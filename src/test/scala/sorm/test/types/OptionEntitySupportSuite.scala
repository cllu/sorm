package sorm.test.types

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import sext._, embrace._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OptionEntitySupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import OptionEntitySupportSuite._

  def entities = Entity[A]() :: Entity[B]() :: Nil
  instancesAndIds foreach { case (db, dbId) =>
    test(dbId + " - save none"){
      db.save(B(None, None))
    }
  }
}
object OptionEntitySupportSuite {

  case class A (var id: Option[Long], a : Int ) extends Persistable
  case class B (var id: Option[Long], a : Option[A] ) extends Persistable

}

