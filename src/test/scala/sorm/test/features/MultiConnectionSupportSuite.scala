package sorm.test.features

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.concurrent.duration._

object MultiConnectionSupportSuite {
  case class A ( a: Int ) extends Persistable
}
@RunWith(classOf[JUnitRunner])
class MultiConnectionSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import MultiConnectionSupportSuite._

  override def entities = Entity[A]() :: Nil
  override def poolSizes = 1 :: 14 :: Nil
  instancesAndIds foreach { case (db, dbId) =>
    test(dbId + " - Entities aren't always stored sequentially"){
      val fs = (1 to 200).map(n => future(db.save(A(n))))
      val rs = fs.map(Await.result(_, 10 seconds)).sortBy(_._id)
      rs should not be ('empty)
      rs.map(_._id) should not equal (rs.map(_.a.toLong))
    }
  }

}
