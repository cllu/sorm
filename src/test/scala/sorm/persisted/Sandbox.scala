package sorm.persisted

object Sandbox extends App {

  case class Genre(name: String)
  case class Artist(name: String, amazonId: Option[String], genres: Set[Genre], tags: Set[String])

  val artist = Artist("Nirvana", Some("saldkfj"), Set(Genre("grunge"), Genre("rock")), Set("kurt", "cobain"))

//  val p = Persisted(artist, 4)
//
//  println(p)
}