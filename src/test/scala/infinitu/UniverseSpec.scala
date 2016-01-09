package infinitu

import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification

object UniverseSpec extends Specification with ShouldMatchers {
  
  "IntegerValue" in {
    val iv0 = IntegerValue()
    val iv0b = IntegerValue(0)
    val iv1 = IntegerValue(1)
    val iv2 = IntegerValue(2)
    
    "must be 0 by default" in {
      iv0.value must be equalTo(0)
      iv0 must be equalTo iv0b
    }

    "summing zeroes should return zero" in {
      iv0 plus iv0b must be equalTo iv0
    }

    "1 + 1 = 2" in {
      iv1 plus iv1 must be equalTo iv2
    }

    "1 - 1 = 0" in {
      iv1 minus iv1 must be equalTo iv0
    }

    "next after 0 = 1" in {
      iv0.next must be equalTo iv1
    }

    "previous before 1 = 0" in { 
      iv1.previous must be equalTo iv0
    }

    "1 > 0" in {
      //why not? iv1 must be greaterThan iv0
      iv1.asInstanceOf[DimensionValue[Integer]] must be greaterThan iv0
    }

    "0 < 1" in {
      iv0.asInstanceOf[DimensionValue[Integer]] must be lessThan iv1
    }
  }

  "IntegerInterval" in {
    val ii00 = IntegerInterval()
    val ii01 = IntegerInterval(0, 1)
    val ii12 = IntegerInterval(1, 2, DimensionIntervalInclude.Right)
    val ii_10 = IntegerInterval(-1, 0, DimensionIntervalInclude.Left)

    "must be all zeroed by default" in {
      ii00.left.value must be equalTo 0
      ii00.right.value must be equalTo 0
      ii00.left must be equalTo IntegerValue()
      ii00.right must be equalTo IntegerValue()
      ii00.size must be equalTo IntegerValue()
    }

    "must include both ends by default" in {
      ii00.include.both must beTrue
    }

    "the next interval from a zero sized one is itself" in {
      ii00.next must be equalTo ii00
    }

    "the next and previous start excluding the direction boundary, including the oposite side incremented by the size" in {
      ii01.next must be equalTo ii12
      ii01.previous must be equalTo ii_10
    }

    "]1,2] > [0,1]" in {
      //why not? iv1 must be greaterThan iv0
      ii12.asInstanceOf[DimensionInterval[Integer]] must be greaterThan ii01
    }

    "[0,1] < ]1,2]" in {
      ii01.asInstanceOf[DimensionInterval[Integer]] must be lessThan ii12
    }

    //add plus to merge continuous intervals?
  }

}
