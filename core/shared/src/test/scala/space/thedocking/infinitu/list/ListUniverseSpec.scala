package space.thedocking.infinitu.list

import space.thedocking.infinitu.dimension.DimensionValue
import org.junit.runner.RunWith
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import space.thedocking.infinitu.integer.IntegerValue

@RunWith(classOf[JUnitRunner])
class ListUniverseSpec extends Specification with ShouldMatchers {

  val v0_0_0 = List(IntegerValue(0), IntegerValue(0), IntegerValue(0))

  val v0_0_1 = List(IntegerValue(0), IntegerValue(0), IntegerValue(1))

  val v0_0_2 = List(IntegerValue(0), IntegerValue(0), IntegerValue(2))

  val vMaxMaxMax =
    List(IntegerValue.MaxValue, IntegerValue.MaxValue, IntegerValue.MaxValue)

  val vMinMinMin =
    List(IntegerValue.MinValue, IntegerValue.MinValue, IntegerValue.MinValue)

  val vMaxPMaxMax = List(IntegerValue.MaxValue,
                         IntegerValue.MaxValue.previous,
                         IntegerValue.MaxValue)

  val vMaxMaxMin =
    List(IntegerValue.MaxValue, IntegerValue.MaxValue, IntegerValue.MinValue)

  "ListUniverse" should {

    "An DiscreteDimensionValueListValue" in {
      val lv0_0_0 = DiscreteDimensionValueListValue(v0_0_0)
      lv0_0_0.value must be equalTo (v0_0_0)

      val lv0_0_0b = DiscreteDimensionValueListValue(v0_0_0)
      lv0_0_0 must be equalTo lv0_0_0b

      lv0_0_0 plus lv0_0_0b must be equalTo lv0_0_0

      val lv0_0_1 = DiscreteDimensionValueListValue(v0_0_1)

      val lv0_0_2 = DiscreteDimensionValueListValue(v0_0_2)

      lv0_0_1 plus lv0_0_1 must be equalTo lv0_0_2

      lv0_0_1 minus lv0_0_1 must be equalTo lv0_0_0

      lv0_0_0.next must be equalTo lv0_0_1

      lv0_0_1.previous must be equalTo lv0_0_0

      val lvMaxMaxMax = DiscreteDimensionValueListValue(vMaxMaxMax)

      val lvMinMinMin = DiscreteDimensionValueListValue(vMinMinMin)

      lvMaxMaxMax.next must be equalTo lvMinMinMin

      lvMinMinMin.previous must be equalTo lvMaxMaxMax

      val lvMaxPMaxMax = DiscreteDimensionValueListValue(vMaxPMaxMax)

      val lvMaxMaxMin = DiscreteDimensionValueListValue(vMaxMaxMin)

      lvMaxPMaxMax.next must be equalTo lvMaxMaxMin

      lvMaxMaxMin.previous must be equalTo lvMaxPMaxMax

      //TODO test relational operators
    }

  }

}
