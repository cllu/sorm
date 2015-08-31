package sorm.test.issues

import java.time.Instant

import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatest._
import sorm.Persistable

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
    val u = db.save(new User(None, json))
    val u2 = db.query[User].fetchOne().get

    implicit val formats = DefaultFormats
    assert((u2.jvalue \ "id").extract[String] == "100")
  }

}
object JValueTest {
  case class User(var id: Option[Long], jvalue: JValue) extends Persistable

  case class UserGroup(var id: Option[Long], name: String, url: String) extends Persistable
  case class Permission(var id: Option[Long], user: User, userGroup: UserGroup, name: String, permission: Boolean)extends Persistable
  case class Site(var id: Option[Long], title: String) extends Persistable
  case class Post(var id: Option[Long], title: String, body: String) extends Persistable
  case class Page(var id: Option[Long], title: String, body: String) extends Persistable
  case class Asset(var id: Option[Long], title: String, bytes: String) extends Persistable
  case class Photo(var id: Option[Long], title: String, bytes: String, url: String) extends Persistable
}