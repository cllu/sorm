package sorm.persisted

import org.slf4j.LoggerFactory
import sext._
import sorm._
import sorm.reflection._

import scala.collection.mutable

object PersistedClass {
  val logger = LoggerFactory.getLogger(getClass.getName)

  import reflect.runtime.universe._
  import scala.tools.reflect.ToolBox

  private var generateNameCounter = 0

  private def generateName() = synchronized {
    generateNameCounter += 1
    "PersistedAnonymous" + generateNameCounter
  }

  private[persisted] def generateCode(r: Reflection, name: String, withReturn:Boolean=true) : String = {
    val sourceArgs: List[(String, Reflection)] = r.primaryConstructorArguments

    val sourceArgSignatures = sourceArgs.view
      .map { case (n, r) => n + " : " + r.signature }
      .toList

    val newArgSignatures: Seq[String] = "val id : Long" +: sourceArgSignatures

    val copyMethodArgSignatures = sourceArgs.map {
      case (n, r) => n + " : " + r.signature + " = " + n
    }

    val oldArgNames = sourceArgs.map {
      _._1
    }

    val newArgNames = "id" +: oldArgNames

    val code = "class " + name + "\n" +
      ("( " + newArgSignatures.mkString(",\n").indent(2).trim + " )\n" +
        "extends " + r.signature + "( " +
        sourceArgs.map {
          _._1
        }.mkString(", ") +
        " )\n" +
        "with " + Reflection[Persisted].signature + "\n" +
        "{\n" +
        (
          "type T = " + r.signature + "\n" +
            "override def mixoutPersisted[ T ]\n" +
            ("= ( id, new " + r.signature + "(" + oldArgNames.mkString(", ") + ").asInstanceOf[T] )").indent(2) + "\n" +
            "override def copy\n" +
            ("( " +
              copyMethodArgSignatures.mkString(",\n").indent(2).trim +
              " )\n" +
              "= " + "new " + name + "( " +
              newArgNames.mkString(", ") +
              " )\n"
              ).indent(2) + "\n" +
//            "override def productElement ( n : Int ) : Any\n" +
//            ("= " +
//              ("n match {\n" +
//                ((for {(n, i) <- newArgNames.view.zipWithIndex}
//                  yield "case " + i + " => " + n
//                  ) :+
//                  "case _ => throw new IndexOutOfBoundsException(n.toString)"
//                  ).mkString("\n").indent(2) + "\n" +
//                "}"
//                ).indent(2).trim
//              ).indent(2) + "\n" +
//            "override def productArity = " + newArgNames.size + "\n" +
            "override def equals ( other : Any )\n" +
            ("= " +
              ("other match {\n" +
                ("case other : " + Reflection[Persisted].signature + " =>\n" + (
                  "id == other.id && super.equals(other)"
                  ).indent(2) + "\n" +
                  "case _ =>\n" +
                  "false".indent(2)
                  ).indent(2) + "\n" +
                "}"
                ).indent(2).trim
              ).indent(2)
          ).indent(2) + "\n" +
        "}")
        .indent(2) + "\n"
    if (withReturn) {
      code + "classOf[" + name + "]\n"
    } else {
      code
    }
  }

  private[persisted] def createClass[T](r: Reflection) : Class[T with Persisted] = {
    val mirror = runtimeMirror(Thread.currentThread().getContextClassLoader)
    val toolbox = mirror.mkToolBox()

      toolbox.eval(
        toolbox.parse(
          generateCode(r, generateName())
            .tap{ c => logger.trace("Generating class:\n" + c) }
        )
      ) .asInstanceOf[Class[T with Persisted]]
    }

  private val classesCache = new {
    private def currentClassLoader = Thread.currentThread().getContextClassLoader

    private var cachedClassLoader = currentClassLoader
    private val map = new collection.mutable.WeakHashMap[Reflection, Class[_ <: Persisted]]

    def resolve(r: Reflection) = synchronized {
      val classLoader = currentClassLoader
      if (classLoader != cachedClassLoader) {
        logger.debug("Classloader changed, discarding PersistedClass cache")
        cachedClassLoader = classLoader
        map.clear()
      }
      map.getOrElseUpdate(r, createClass(r))
    }

    def resolve(r: Reflection, cls: Class[_ <: AnyRef with Persisted]): Unit = {
      map.update(r, cls)
    }
  }

  def apply(r: Reflection) = classesCache.resolve(r.mixinBasis)

  // pre-cache all entities
  def applyAll(entities: Traversable[Entity]) = {

    var code = ""
    var names = new mutable.ListBuffer[String]
    var reflections = new mutable.ListBuffer[Reflection]
    for (entity <- entities) {
      val r = entity.reflection
      val name = generateName()
      names += s"classOf[$name]"
      reflections += r
      code += generateCode(r, name, withReturn = false)
    }

    val mirror = runtimeMirror(Thread.currentThread().getContextClassLoader)
    val toolbox = mirror.mkToolBox()

    code += "List(" + names.mkString(", ") + ")"
    //println(code)

    val tree = toolbox.parse(code)
    val start = System.currentTimeMillis()
    val t = toolbox.eval(tree).asInstanceOf[List[Class[_ <: AnyRef with Persisted]]]
    for ((cls, reflection) <- t.zip(reflections)) {
      classesCache.resolve(reflection, cls)
    }
    println(s"apply all using: ${System.currentTimeMillis()-start}")

  }
}
