package sorm.test.types

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import sorm._
import sext._, embrace._
import sorm.test.MultiInstanceSuite

@RunWith(classOf[JUnitRunner])
class OptionEntitySeqItemSupportSuite extends FunSuite with ShouldMatchers with MultiInstanceSuite {

  import OptionEntitySeqItemSupportSuite._

  def entities = Set() + Entity[A]() + Entity[B]()
  instancesAndIds foreach { case (db, dbId) =>
    val b1 = db.save(B(None, "abc"))
    val b2 = db.save(B(None, "cba"))

    val a1 = db.save(A(None, Seq() ))
    val a2 = db.save(A(None, Seq(Some(b1), None, Some(b2)) ))
    val a3 = db.save(A(None, Seq(None, Some(b2)) ))
    val a4 = db.save(A(None, Seq(None) ))

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
  case class A (var id: Option[Long], seq : Seq[Option[B]]) extends Persistable
  case class B (var id: Option[Long], z : String ) extends Persistable
}