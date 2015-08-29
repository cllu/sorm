package sorm.joda

import java.time.{LocalDate, LocalTime, Instant}

object Extensions {

  implicit class DateTimeToJava ( val self : Instant ) extends AnyVal {
    def toJava = new java.sql.Timestamp( self.toEpochMilli )
  }

  implicit class LocalDateToJava ( val self : LocalDate ) extends AnyVal {
    def toJava = new java.sql.Date(self.getYear, self.getMonth.getValue-1, self.getDayOfMonth)
  }

  implicit class LocalTimeToJava ( val self : LocalTime ) extends AnyVal {
    def toJava = new java.sql.Time(self.getHour, self.getMinute, self.getSecond)
  }

}