package sorm.test.features

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class InFilterTest extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import InFilterTest._

  def entities = Set() + Entity[A]()

  instancesAndIds foreach { case (db, dbId) =>

    val a1 = db.save(A(1))
    val a2 = db.save(A(2))
    val a3 = db.save(A(3))

    test(dbId + " - empty value"){
      db.query[A].whereIn("a", Seq()).fetchOne().should(equal(None))
    }
    test(dbId + " - valid value"){
      db.query[A].whereIn("a", Seq(2)).fetchOne().should(equal(Some(a2)))
    }

  }

}
object InFilterTest {

  case class A ( a : Int ) extends Persistable

}