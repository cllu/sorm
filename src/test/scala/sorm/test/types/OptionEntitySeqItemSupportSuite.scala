package sorm.test.types

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import sorm._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OptionEntitySeqItemSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {

  import OptionEntitySeqItemSupportSuite._

  def entities = Set() + Entity[A]() + Entity[B]()
  instancesAndIds foreach { case (db, dbId) =>
    val b1 = db.save(B( "abc"))
    val b2 = db.save(B( "cba"))

    val a1 = db.save(A(Seq() ))
    val a2 = db.save(A(Seq(Some(b1), None, Some(b2)) ))
    val a3 = db.save(A(Seq(None, Some(b2)) ))
    val a4 = db.save(A(Seq(None) ))

    test(dbId + " - empty seq"){
      db.fetchById[A](a1.id.get).seq should be === Seq()
    }
    test(dbId + " - seq of none"){
      db.fetchById[A](a4.id.get).seq should be === Seq(None)
    }
    test(dbId + " - not empty seqs are correct"){
      db.fetchById[A](a2.id.get).seq should be === Seq(Some(b1), None, Some(b2))
      db.fetchById[A](a3.id.get).seq should be === Seq(None, Some(b2))
    }
  }
}
object OptionEntitySeqItemSupportSuite {
  case class A ( seq : Seq[Option[B]]) extends Persistable
  case class B ( z : String ) extends Persistable
}