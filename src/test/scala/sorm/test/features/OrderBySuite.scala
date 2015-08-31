package sorm.test.features

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OrderBySuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import OrderBySuite._

  def entities = Set(Entity[A]())
  instancesAndIds foreach { case (db, dbId) =>
    val data = 1 to 10
    data.foreach(v => db.save(A(v)))
    test(dbId + " - works"){
      db.query[A].order("a").fetch().map(_.a)
        .should(equal(data))
    }
  }
}
object OrderBySuite {
  case class A ( a : Int ) extends Persistable
}