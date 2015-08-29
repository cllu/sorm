package sorm.mappings

import sext._, embrace._

import sorm._
import reflection.Reflection
import core._

object `package` {

  val regex = "([a-z])([A-Z]+)"
  val replacement = "$1_$2"

  def ddlName ( string : String ) : String = {
//    import com.google.common.base.CaseFormat._
//    UPPER_CAMEL.to( LOWER_UNDERSCORE, string )

    string.replaceAll(regex, replacement).toLowerCase
    }

  case class EntitySettings
    ( indexes       : Set[Seq[String]] = Set.empty,
      uniqueKeys    : Set[Seq[String]] = Set.empty )

}