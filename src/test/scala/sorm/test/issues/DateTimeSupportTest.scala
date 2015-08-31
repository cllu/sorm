package sorm.test.issues

import java.time.Instant

import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.scalatest._
import sorm.Persistable

@org.junit.runner.RunWith(classOf[junit.JUnitRunner])
class DateTimeSupportTest extends FunSuite with ShouldMatchers {
  import sorm._
  import DateTimeSupportTest._

  test("JValue Test"){
    val db = new Instance (
      entities = Set(Entity[User]()),
      url = "jdbc:h2:mem:test",
      initMode = InitMode.Create,
      poolSize = 20
    )
    val u = db.save(new User(None, Instant.now))
    val u2 = db.query[User].fetchOne().get

    println(u2.created)
  }

}
object DateTimeSupportTest {
  case class User(var id: Option[Long], created: Instant) extends Persistable
}