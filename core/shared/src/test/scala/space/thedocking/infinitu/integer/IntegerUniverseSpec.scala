package space.thedocking.infinitu.integer

import space.thedocking.infinitu.dimension.DimensionValue
import org.junit.runner.RunWith
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IntegerUniverseSpec extends Specification with ShouldMatchers {

  "IntegerUniverse" should {

    "define an IntegerValue" in {
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

    "define an IntegerIntervalDimension" in {
      val iid = IntegerIntervalDimension(name = "small test dimension",
                                         minValue = IntegerValue(),
                                         maxValue = IntegerValue(2))
      iid must not beNull;
      iid.plus(IntegerValue(), IntegerValue(1)) must be equalTo IntegerValue(1)
    }

  }

}
