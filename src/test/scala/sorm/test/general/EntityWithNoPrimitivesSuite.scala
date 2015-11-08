package sorm.test.general

import org.scalatest._
import sorm._

@org.junit.runner.RunWith(classOf[junit.JUnitRunner])
class EntityWithNoPrimitivesSuite extends FunSuite with ShouldMatchers {
  import EntityWithNoPrimitivesSuite._

  test("All is fine"){
    val db = new Instance(
      entities = Set(
        Entity[Artist]()
      ),
      url = "jdbc:h2:mem:test",
      user = "",
      password = "",
      initMode = InitMode.DropAllCreate
    )

    db.save(Artist( Set("metal"),Set("foo"),Set("bar")))

    // Copy do not work!
//    db.query[Artist]
//      .whereEqual("id", 1)
//      .fetchOne()
//      .map(a => a.copy(genres = Set("rock","rnb"), a = Set("myfoo"), b = Set("mybar")))
//      .map(db.save)

    val node = db.query[Artist]
      .whereEqual("_id", 1)
      .fetchOne()
      .get
    val n2 = node.copy(genres = Set("rock","rnb"), a = Set("myfoo"), b = Set("mybar"))
    n2._id = node._id
    db.save(n2)

    db.query[Artist].whereEqual("_id", 1).fetchOne().map(_.b).shouldBe(Some(Set("mybar")))
  }
}
object EntityWithNoPrimitivesSuite {
  case class Artist( genres: Set[String] , a: Set[String], b: Set[String] ) extends Persistable

}