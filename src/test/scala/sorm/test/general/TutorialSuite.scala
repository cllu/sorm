package sorm.test.general

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import sorm.persisted.Persisted
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class TutorialSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {

  import TutorialSuite._

  def entities
    = Set() +
      Entity[Artist]() +
      Entity[Genre]() +
      Entity[Locale](unique = Set() + Seq("code"))

  instancesAndIds foreach { case (db, dbId) =>
    //  create locales:
    val ru = db.save( Locale(None, "ru") )
    val en = db.save( Locale(None, "en") )

    //  create genres:
    val rock      = db.save( Genre(None,  Map( en -> Seq("Rock"),
                                         ru -> Seq("Рок") ) ) )
    val hardRock  = db.save( Genre(None, Map( en -> Seq("Hard Rock"),
                                         ru -> Seq("Тяжёлый рок",
                                                   "Тяжелый рок") ) ) )
    val metal     = db.save( Genre(None,  Map( en -> Seq("Metal"),
                                         ru -> Seq("Метал") ) ) )
    val grunge    = db.save( Genre(None,  Map( en -> Seq("Grunge"),
                                         ru -> Seq("Грандж") ) ) )

    //  create artists:
    db.save( Artist(None, Map( en -> Seq("Metallica"),
                          ru -> Seq("Металика", "Металлика") ),
                     Set( metal, rock, hardRock ) ) )
    db.save( Artist(None, Map( en -> Seq("Nirvana"),
                          ru -> Seq("Нирвана") ),
                     Set( rock, hardRock, grunge ) ) )
    db.save( Artist(None, Map( en -> Seq("Kino"),
                          ru -> Seq("Кино") ),
                     Set( rock ) ) )
    db.save( Artist(None, Map( en -> Seq("The Rolling Stones",
                                    "Rolling Stones",
                                    "Rolling Stones, The"),
                          ru -> Seq("Ролинг Стоунз",
                                    "Роллинг Стоунз",
                                    "Роллинг Стоунс",
                                    "Ролинг Стоунс") ),
                     Set( rock ) ) )
    db.save( Artist(None, Map( en -> Seq("Dire Straits"),
                          ru -> Seq("Даэр Стрэйтс") ),
                     Set( rock ) ) )
    db.save( Artist(None, Map( en -> Seq("Godsmack"),
                          ru -> Seq("Годсмэк") ),
                     Set( metal, hardRock, rock ) ) )

    //  All artists having a genre equaling to the value of the `metal` variable,
    //  which we've previously declared.
    //  The result type is `Stream[Artist with Persisted]`
    val metalArtists = db.query[Artist].whereContains("genres", metal).fetch()

    //  All artists having a genre that contains "Hard Rock" of a locale with a
    //  code "en" in a list of its names.
    //  The result type is `Stream[Artist with Persisted]`
    val hardRockArtists
      = db.query[Artist]
          .whereEqual("genres.item.names.value.item", "Hard Rock")
          .whereEqual("genres.item.names.key.code", "en")
          .fetch()

    test(dbId + " - metal artists"){
      metalArtists.flatMap(_.names.values.flatten) should (
        contain("Metallica") and contain("Godsmack") and not contain("Kino")
      )
    }
    test(dbId + " - hard rock artists"){
      hardRockArtists.flatMap(_.names.values.flatten) should (
        contain("Nirvana") and contain("Metallica") and contain("Godsmack")
        and not contain("Dire Straits")
      )
    }
  }
}
object TutorialSuite {
  case class Artist (var id: Option[Long], names : Map[Locale, Seq[String]],
      genres : Set[Genre] ) extends Persistable

  case class Genre
    (var id: Option[Long], names: Map[Locale, Seq[String]]) extends Persistable

  case class Locale
    (var id: Option[Long], code : String ) extends Persistable
}