package sorm.persisted

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm.persisted.PersistedSuite._

@RunWith(classOf[JUnitRunner])
class PersistedSuite extends FunSuite with ShouldMatchers {

  // TODO: After delete two methods: productElement and productArity, this test would fail
  test("Different persisted ids make otherwise equaling objects have different hashcodes") {
    pending
    //Persisted(Genre("a"), 1).hashCode should not equal(Persisted(Genre("a"), 2).hashCode)
  }
  test("Different persisted ids make otherwise equaling objects unequal") {
//    Persisted(Genre("a"), 1) should not equal(Persisted(Genre("a"), 2))
  }
  test("Same persisted ids keep equaling objects equal") {
//    Persisted(Genre("a"), 1) should equal(Persisted(Genre("a"), 1))
  }
//  test("all is fine") {
//
//
//    val artist = Artist("Nirvana", Set(Genre("rock"), Genre("grunge")))
//
//
//    val instance = Persisted(artist, 24)
//
//    assert(instance.isInstanceOf[Persistable], "is not Persisted")
//    assert(instance.isInstanceOf[Artist], "is not Artist")
//    assert(instance.id == 24, "incorrect id")
//    assert(instance.name == "Nirvana", "incorrect properties")
//
//    val copy = instance.copy(name = "Puddle of Mudd")
//
//    assert(copy.isInstanceOf[Persistable])
//    assert(copy.isInstanceOf[Artist])
//    //  the only problem: compiler thinks that `copy` does not implement `Persisted`, so to access `key` we have to specify it manually:
//    assert(copy.asInstanceOf[Artist with Persistable].id == 24)
//    assert(copy.name == "Puddle of Mudd")
//    assert(copy != instance)
//
//
//  }
  test("dynamic persisted fails on incorrect map") {
    evaluating {Persisted[Artist](Map("name" -> "Nirvana"), 35)} should produce[Exception]
  }
  test("persisted on persisted") {
//    evaluating { Persisted(Persisted(Artist("Nirvana", Set()), 2), 4) }
//      .should( produce[Exception])
//      .getMessage should be ("Persisted on persisted called")
  }
}
object PersistedSuite {

  case class Artist(name: String, genres: Set[Genre])

  case class Genre(name: String)
}