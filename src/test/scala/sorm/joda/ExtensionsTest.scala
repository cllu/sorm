package sorm.joda

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class ExtensionsTest extends FunSuite with ShouldMatchers {

  test("Middle ages LocalDate back and forth"){
//    new LocalDate(1200, 1, 1).toJava.toJoda shouldBe new LocalDate(1200, 1, 1)
  }
  test("Middle ages LocalDate toJava"){
//    new LocalDate(1200, 1, 1).toJava.toString shouldBe "1200-01-01"
  }

}
