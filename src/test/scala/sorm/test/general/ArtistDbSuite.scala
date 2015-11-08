package sorm.test.general

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

object ArtistDbSuite {

  case class Artist ( names : Map[Locale, Seq[String]],
      styles : Set[Style] ) extends Persistable

  case class Style
    ( names : Map[Locale, Seq[String]] ) extends Persistable

  case class Locale
    ( code : String ) extends Persistable

}
@RunWith(classOf[JUnitRunner])
class ArtistDbSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {

  import ArtistDbSuite._

  def entities = Set() + Entity[Artist]() + Entity[Style]() + Entity[Locale]()

  instancesAndIds foreach { case (db, dbId) =>
    val ru = db.save( Locale( "ru") )
    val en = db.save( Locale( "en") )

    val rock
      = db.save( Style( Map( en -> Seq("Rock"),
                             ru -> Seq("Рок") ) ) )
    val hardRock
      = db.save( Style( Map( en -> Seq("Hard Rock"),
                             ru -> Seq("Тяжёлый рок", "Тяжелый рок") ) ) )
    val metal
      = db.save( Style( Map( en -> Seq("Metal"),
                             ru -> Seq("Метал") ) ) )
    val grunge
      = db.save( Style( Map( en -> Seq("Grunge"),
                             ru -> Seq("Грандж") ) ) )

    val metallica
      = db.save( Artist( Map( en -> Seq("Metallica"),
                              ru -> Seq("Металика", "Металлика") ),
                         Set( metal, rock, hardRock ) ) )
    val nirvana
      = db.save( Artist( Map( en -> Seq("Nirvana"),
                              ru -> Seq("Нирвана") ),
                         Set( rock, hardRock, grunge ) ) )
    val kino
      = db.save( Artist( Map( en -> Seq("Kino"),
                              ru -> Seq("Кино") ),
                         Set( rock ) ) )
    val rollingStones
      = db.save( Artist( Map( en -> Seq("The Rolling Stones",
                                        "Rolling Stones",
                                        "Rolling Stones, The"),
                              ru -> Seq("Ролинг Стоунз",
                                        "Роллинг Стоунз",
                                        "Роллинг Стоунс",
                                        "Ролинг Стоунс") ),
                         Set( rock ) ) )
    val direStraits
      = db.save( Artist( Map( en -> Seq("Dire Straits"),
                              ru -> Seq("Даэр Стрэйтс") ),
                         Set( rock ) ) )
    val godsmack
      = db.save( Artist( Map( en -> Seq("Godsmack"),
                              ru -> Seq("Годсмэк") ),
                         Set( metal, hardRock, rock ) ) )


    test(dbId + " - path with index"){
      db.query[Artist]
        .whereEqual("names.value(1)", "Rolling Stones")
        .fetchOne()
        .get
        .names(en)(1) should be === "Rolling Stones"
    }
    test(dbId + " - Offset"){
      pending
    }
    test(dbId + " - Limit"){
      pending
    }
    test(dbId + " - Ordering"){
      db.query[Artist].order("_id", true).fetch().map(_._id) should equal (godsmack._id :: direStraits._id :: rollingStones._id :: kino._id :: nirvana._id :: metallica._id :: Nil)
    }
    test(dbId + " - Contains"){
      pending
    }
    test(dbId + " - Equality to unpersisted entity"){
      pending
    }
    test(dbId + " - Equality to persisted entity"){
      db.query[Artist]
        .whereEqual("styles.item", metal)
        .fetch()
        .flatMap{_.names.values.head}
        .toSet should be === Set("Metallica", "Godsmack")
    }
    test(dbId + " - Map, Set, Seq deep path"){
      db.query[Artist]
        .whereEqual("styles.item.names.value.item", "Hard Rock")
        .fetch()
        .flatMap{_.names.values.head}
        .toSet should be === Set("Metallica", "Nirvana", "Godsmack")
    }
    test(dbId + " - Results have correct id property"){
      db.query[Artist].fetchOne().map{_._id} should be === Some(metallica._id)
    }
    test(dbId + " - Query by id"){
      db.query[Artist].whereEqual("_id", metallica._id.get).fetchOne()
        .map{_.names.values.head.head}.get should be === "Metallica"
      db.query[Artist].whereEqual("_id", kino._id.get).fetchOne()
        .map{_.names.values.head.head}.get should be === "Kino"
    }
  }
}
