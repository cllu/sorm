package sorm.test.issues

import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatest._

@org.junit.runner.RunWith(classOf[junit.JUnitRunner])
class LazyValTest extends FunSuite with ShouldMatchers {
  import sorm._
  import LazyValTest._

  test("LazyVal and ImplicitVal Test"){
    val db = new Instance (
      entities = Set(Entity[User]()),
      url = "jdbc:h2:mem:testLazyVal",
      initMode = InitMode.Create,
      poolSize = 20
    )
    val json = ("name" -> "XXX")
    val u = db.save(new User(json))
    val u2 = db.query[User].fetchOne().get

    intercept[NoSuchElementException] {
      db.query[User].whereEqual("name", "XXX").fetchOne().get
    }
    intercept[NoSuchElementException] {
      db.query[User].whereEqual("format", "XXX").fetchOne().get
    }
    assert(u2.name == "XXX")
  }

}
object LazyValTest {
  case class User(jvalue: JValue) {

    implicit val formats = DefaultFormats
    lazy val name = (jvalue \ "name").extract[String]
  }
}