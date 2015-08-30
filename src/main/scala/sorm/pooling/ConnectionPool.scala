package sorm.pooling

import sorm.jdbc.JdbcConnection

trait ConnectionPool {
  protected def fetchConnection () : JdbcConnection
  protected def returnConnection ( c : JdbcConnection )

  private val openConnection = new ThreadLocal[Option[JdbcConnection]] {
    override def initialValue() = None
  }

  def withConnection [ T ] ( f : JdbcConnection => T ) : T
    = {
      openConnection.get match {
        case Some(cx) =>
          f(cx)
        case None =>
          val cx = fetchConnection()
          openConnection.set(Some(cx))
          try f(cx)
          finally {
            openConnection.set(None)
            returnConnection(cx)
          }
      }


    }


}
