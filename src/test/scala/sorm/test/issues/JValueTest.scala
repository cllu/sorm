package sorm.test.issues

import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatest._

@org.junit.runner.RunWith(classOf[junit.JUnitRunner])
class JValueTest extends FunSuite with ShouldMatchers {
  import sorm._
  import JValueTest._

  test("JValue Test"){
    val db = new Instance (
      entities = Set(Entity[User]()),
      url = "jdbc:h2:mem:test",
      initMode = InitMode.Create,
      poolSize = 20
    )
    val json = ("id" -> "100")
    val u = db.save(new User(json))
    val u2 = db.query[User].fetchOne().get

    implicit val formats = DefaultFormats
    assert((u2.json \ "id").extract[String] == "100")
  }

}
object JValueTest {
  case class User(json: JValue)
}