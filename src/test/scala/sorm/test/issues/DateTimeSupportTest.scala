package sorm.test.issues

import java.time.Instant

import org.scalatest._
import sorm.Persistable

@org.junit.runner.RunWith(classOf[junit.JUnitRunner])
class DateTimeSupportTest extends FunSuite with ShouldMatchers {
  import DateTimeSupportTest._
  import sorm._

  test("JValue Test"){
    val db = new Instance (
      entities = Set(Entity[User]()),
      url = "jdbc:h2:mem:test",
      initMode = InitMode.Create,
      poolSize = 20
    )
    val u = db.save(new User(Instant.now))
    val u2 = db.query[User].fetchOne().get

    println(u2.created)
  }

}
object DateTimeSupportTest {
  case class User( created: Instant) extends Persistable
}