package sorm.driver

import java.time.Instant

import sorm.jdbc.{JdbcConnection, Statement}

trait StdNow { self: StdConnection =>
  def now() : Instant
    = connection
        .executeQuery(Statement("SELECT NOW()"))()
        .head.head
        .asInstanceOf[Instant]

}
