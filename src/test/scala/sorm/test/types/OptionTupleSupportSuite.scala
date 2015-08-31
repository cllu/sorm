package sorm.test.types

/*
 * Tuple support is broken
@RunWith(classOf[JUnitRunner])
class OptionTupleSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {

  import OptionTupleSupportSuite._

  def entities = Set() + Entity[A]()
  instancesAndIds foreach { case (db, dbId) =>
    val a1 = db.save(A(None ))
    val a2 = db.save(A(Some(2 -> None) ))
    val a3 = db.save(A(Some(56 -> Some("asdf")) ))

    test(dbId + " - top none"){
      db.fetchById[A](a1.id.get).a should be === None
    }
    test(dbId + " - deep none"){
      db.fetchById[A](a2.id.get).a should be === Some(2 -> None)
    }
    test(dbId + " - deep some"){
      db.fetchById[A](a3.id.get).a should be === Some(56 -> Some("asdf"))
    }
  }

}
object OptionTupleSupportSuite {

  case class A
    ( a : Option[(Int, Option[String])] ) extends Persistable

}
*/