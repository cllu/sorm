package sorm.test.types

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class BooleanSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import BooleanSupportSuite._

  def entities = Set(Entity[A]())
  instancesAndIds foreach { case (db, dbId) =>
    val seq = true :: true :: false :: true :: false :: Nil
    seq.foreach(v => db.save(A(v)))
    test(dbId + " - fetching"){
      db.query[A].order("_id").fetch().map(_.boo)
        .should(equal(seq))
    }
    test(dbId + " - filtering"){
      db.query[A].whereEqual("boo", true).fetch()
        .should(have('size(seq.count(_ == true))))
    }
  }
}
object BooleanSupportSuite {
  case class A ( boo : Boolean ) extends Persistable
}