package sorm.mappings

import embrace._
import org.json4s.JValue
import org.json4s.jackson.JsonMethods.{compact, parse, render}
import sorm.ddl._
import sorm.driver.DriverConnection
import sorm.reflection._

class JValueMapping
  ( val reflection : Reflection,
    val membership : Option[Membership],
    val settings : Map[Reflection, EntitySettings] )
  extends ColumnMapping
  {
    def columnType = ColumnType.Text
    def valueFromContainerRow ( data : String => Any, connection : DriverConnection )
      = parse(data(memberName).asInstanceOf[String])
    def valuesForContainerTableRow( value : Any )
      = compact(render(value.asInstanceOf[JValue])) $ (memberName -> _) $ (Stream(_))
  }