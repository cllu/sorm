package sorm.test.types

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OptionEntitySupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import OptionEntitySupportSuite._

  def entities = Entity[A]() :: Entity[B]() :: Nil
  instancesAndIds foreach { case (db, dbId) =>
    test(dbId + " - save none"){
      db.save(B( None))
    }
  }
}
object OptionEntitySupportSuite {

  case class A ( a : Int ) extends Persistable
  case class B ( a : Option[A] ) extends Persistable

}

