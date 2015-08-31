package sorm.mappings

import embrace._
import sorm._
import sorm.core._
import sorm.driver.DriverConnection
import sorm.jdbc.ResultSetView
import sorm.persisted.Persisted
import sorm.reflection.Reflection

class EntityMapping
  ( val reflection : Reflection,
    val membership : Option[Membership],
    val settings : Map[Reflection, EntitySettings] )
  extends MasterTableMapping {

  lazy val properties = {
    val properties = reflection.properties
    properties.filter(p => !Seq("id", "id_$eq").contains(p._1)).map{
      case (n, r) => n -> Mapping(r, Membership.EntityProperty(n, this), settings)
    }
  }
  lazy val mappings // todo: add id
    = properties.values.toStream
  lazy val primaryKeyColumns = id.column +: Stream()
  lazy val id = new ValueMapping(Reflection[Long], Some(Membership.EntityId(this)), settings)
  lazy val generatedColumns = id.column +: Stream()

  def parseResultSet(rs: ResultSetView, c: DriverConnection)
    = rs.byNameRowsTraversable.toStream
        .headOption
        .map( row => Persisted(
          properties.mapValues( _.valueFromContainerRow(row, c) ),
          row("id") $ Util.toLong,
          reflection
        ) )
        .get

  def delete ( node : Persistable, connection : DriverConnection ) {
    node.id match {
      case Some(id) =>
        ("id" -> id) $ (Stream(_)) $ (tableName -> _) $$ connection.delete
      case None =>
        throw new SormException("Attempt to delete an unpersisted entity: " + node)
    }
  }

  def valuesForContainerTableRow(value : Any)
    = value match {
        case value : Persistable =>
          ( memberName + "$id" -> value.id.get ) +: Stream()
        case _ =>
          throw new SormException("Attempt to refer to an unpersisted entity: " + value)
      }

  def save (node : Persistable, connection : DriverConnection): Persistable
    = {
      val propertyValues = properties.map{ case (n, m) => (n, m, reflection.propertyValue(n, node.asInstanceOf[AnyRef])) }.toStream
      val rowValues = propertyValues.flatMap{ case (n, m, v) => m.valuesForContainerTableRow(v) }

      node.id match {
        case Some(id) =>
          // update
          val pk = Stream(id)
          connection.update(tableName, rowValues, pk $ (primaryKeyColumnNames zip _))
          propertyValues.foreach{ case (n, m, v) => m.update(v, pk, connection) }
          node
        case None =>
          // insert
          val id = connection.insertAndGetGeneratedKeys(tableName, rowValues).ensuring(_.length == 1).head.asInstanceOf[Long]
          propertyValues.foreach{ case (n, m, v) => m.insert(v, Stream(id), connection) }
          Persisted( propertyValues.map(t => t._1 -> t._3).toMap, id, reflection )
      }
    }

  override lazy val uniqueKeysColumnNames
    = settings get reflection map (_.uniqueKeys) getOrElse Set() map (_ map properties flatMap (_.columnsForContainer.map(_.name))) filter (_.nonEmpty)

  override lazy val indexesColumnNames
    = settings get reflection map (_.indexes) getOrElse Set() map (_ map properties flatMap (_.columnsForContainer.map(_.name))) filter (_.nonEmpty)

  lazy val uniqueKeys
    = settings get reflection map (_.uniqueKeys) getOrElse Set()

  lazy val indexes
    = settings get reflection map (_.indexes) getOrElse Set()
}