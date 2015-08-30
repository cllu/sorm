package sorm.test.issues

import java.time.Instant

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
      entities = Set(Entity[User](), Entity[UserGroup](), Entity[Permission](),
        Entity[Site](), Entity[Post](), Entity[Page](), Entity[Asset](), Entity[Photo]()),
      url = "jdbc:h2:mem:testJValue",
      initMode = InitMode.Create,
      poolSize = 20
    )
    val json = ("id" -> "100")
    val u = db.save(new User(json))
    val u2 = db.query[User].fetchOne().get

    implicit val formats = DefaultFormats
    assert((u2.jvalue \ "id").extract[String] == "100")
  }

}
object JValueTest {
  case class User(jvalue: JValue)

  case class UserGroup(name: String, url: String)
  case class Permission(user: User, userGroup: UserGroup, name: String, permission: Boolean)
  case class Site(title: String)
  case class Post(title: String, body: String)
  case class Page(title: String, body: String)
  case class Asset(title: String, bytes: String)
  case class Photo(title: String, bytes: String, url: String)
}