package sorm.test.types

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OptionSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {
  import OptionSupportSuite._

  def entities = Entity[EntityWithOptionInOption]() :: Nil

}
object OptionSupportSuite {

  case class EntityWithOptionInOption
    ( optionInOption : Option[Option[Int]] )

}

