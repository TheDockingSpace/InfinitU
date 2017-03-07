package infinitu.universe

import infinitu.dimension._
import infinitu.integer._
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification

object UniverseSpec extends Specification with ShouldMatchers {

  "An IntegerValue" in {
    val iv = IntegerValue()
    iv.value must be equalTo (0)

    val iv2 = IntegerValue(0)
    iv must be equalTo iv2

    iv plus iv2 must be equalTo iv

    val iv3 = IntegerValue(1)
    iv3 plus iv3 must be equalTo IntegerValue(2)

    iv3 minus iv3 must be equalTo iv

    iv.next must be equalTo iv3

    iv3.previous must be equalTo iv

    //why not? iv3 must be greaterThan iv
    iv3.asInstanceOf[DimensionValue[Integer]] must be greaterThan iv
    iv.asInstanceOf[DimensionValue[Integer]] must be lessThan iv3
  }

}
