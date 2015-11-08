package sorm.test.features

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class ParallelSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import ParallelSuite._

  def entities = Set(Entity[A]())
  instancesAndIds foreach { case (db, dbId) =>
    val data = 10 to 200
    data.par.foreach(v => db.save(A(v)))
    test(dbId + " - fetching"){
      db.query[A].order("_id").fetch().map(_.a).toSet
        .should(equal(data.toSet))
    }
  }

}
object ParallelSuite {
  case class A ( a : Int ) extends Persistable
}