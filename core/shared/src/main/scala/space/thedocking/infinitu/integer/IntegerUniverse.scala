package space.thedocking.infinitu.integer

import space.thedocking.infinitu.dimension._
import space.thedocking.infinitu.universe._
import java.lang.{Boolean => JBoolean}
import scala.math.pow

case class IntegerValue(override val value: Integer = 0)
    extends DiscreteDimensionValue[Integer] {

  override lazy val maxValue = IntegerValue.MaxValue

  override lazy val minValue = IntegerValue.MinValue

  override def plus(other: DimensionValue[_]): IntegerValue =
    IntegerValue(value + other.value.asInstanceOf[Integer])

  override def minus(other: DimensionValue[_]): IntegerValue =
    IntegerValue(value - other.value.asInstanceOf[Integer])

  override def next: IntegerValue = IntegerValue(value + 1)

  override def previous: IntegerValue = IntegerValue(value - 1)

}

case object IntegerValue {

  lazy val MaxValue = IntegerValue(Integer.MAX_VALUE)

  lazy val MinValue = IntegerValue(Integer.MIN_VALUE)

  def apply(v: Int): IntegerValue = IntegerValue(Integer.valueOf(v))

}

case class IntegerInterval(override val left: IntegerValue,
                           override val right: IntegerValue,
                           override val include: DimensionIntervalInclude =
                             DimensionIntervalInclude.Both)
    extends DiscreteDimensionInterval[Integer] {

  override def next: IntegerInterval =
    IntegerInterval(right, right.plus(size), if (include.right) {
      DimensionIntervalInclude.Right
    } else {
      DimensionIntervalInclude.Left
    })

  override def previous: IntegerInterval =
    IntegerInterval(left, left.minus(size), if (include.left) {
      DimensionIntervalInclude.Left
    } else {
      DimensionIntervalInclude.Right
    })

}

case class IntegerDimension(override val name: String)
    extends DiscreteDimension[Integer] {

  override val origin                            = IntegerValue(0)
  override val minValue: DimensionValue[Integer] = IntegerValue.MinValue
  override val maxValue: DimensionValue[Integer] = IntegerValue.MaxValue
}

case class IntegerIntervalDimension(
    override val name: String,
    override val origin: DiscreteDimensionValue[Integer] = IntegerValue(0),
    override val minValue: DimensionValue[Integer] = IntegerValue.MinValue,
    override val maxValue: DimensionValue[Integer] = IntegerValue.MaxValue)
    extends DiscreteDimension[Integer] {

  //TODO move to IntervalDimension trait and/or reuse other interval related code, adding other logic operators
  def contains(value: Int) = minValue.value <= value && value <= maxValue.value

  val size = maxValue.value - minValue.value

  private def offset(value: Int) = {
    val cycles = value / size
    value - (cycles * size)
  }

  private def localValue(value: Int) = {
    if (contains(value)) value
    else {
      val off = offset(value)
      if (value < 0) {
        maxValue.value - off
      } else {
        minValue.value + off
      }
    }
  }

  override def minus(value: DimensionValue[Integer],
                     other: DimensionValue[_]): DimensionValue[Integer] = {
    val result = (value, other) match {
      case (IntegerValue(v1), IntegerValue(v2)) if v1 == v2 => origin
      case (IntegerValue(v1), IntegerValue(v2)) =>
        localValue(localValue(v1) - localValue(v2))
      case _ =>
        throw new RuntimeException(
          s"The current implementation is not ready to do $value minus $other")
    }
    result.asInstanceOf[DimensionValue[Integer]]
  }

  //TODO keep track of turns ("z + b = ba")
  override def plus(value: DimensionValue[Integer],
                    other: DimensionValue[_]): DimensionValue[Integer] = {
    val result = (value, other) match {
      case (IntegerValue(v1), IntegerValue(v2)) if v1 == v2 => origin
      case (IntegerValue(v1), IntegerValue(v2)) =>
        IntegerValue(localValue(v1) + localValue(v2))
      case _ =>
        throw new RuntimeException(
          s"The current implementation is not ready to do $value plus $other")
    }
    result
  }

}

case class Integer1DObjectAddress(val firstValue: Integer = 0,
                                  override val dimensions: List[IntegerDimension] =
                                    List(IntegerDimension("x")))
    extends ObjectAddress {

  override val values =
    List(IntegerValue(firstValue))
    
  val dimension = dimensions.head.asInstanceOf[IntegerDimension]
  val integerValue = valueAt(dimension).value

  override def withValues[A <: ObjectAddress](
      values: List[DimensionValue[_]]): A =
    Integer1DObjectAddress(values(0).asInstanceOf[Integer], dimensions)
      .asInstanceOf[A]

}

class Integer1DUniverse[V <: Comparable[_]](
    override val name: String = "Integer1DUniverse",
    val dimensionName: String = "x",
    override val objects: Map[Integer1DObjectAddress,V] = Map.empty)
    extends Universe[Integer1DObjectAddress, V] {

  override val dimensions = List(IntegerDimension(dimensionName))

  override def withObjects(
      objects: Map[Integer1DObjectAddress, V]): Integer1DUniverse[V] = {
    new Integer1DUniverse(name, dimensionName, objects)
  }

}

object Integer1DUniverse {
  def empty[V <: Comparable[_]] = new Integer1DUniverse[V](objects = Map.empty)
}

class IntegerBoolean1DUniverse(
  override val name: String = "IntegerBoolean1DUniverse",
  override val dimensionName: String = "bits",
  val bits: Map[Integer1DObjectAddress, JBoolean] = Map.empty)
    extends Integer1DUniverse[JBoolean](objects = bits) {
  lazy val bitStringValue = values.map(if (_) "1" else "0").mkString("")
  lazy val longValue = {
    val dIter = bits.map {
      case (a: Integer1DObjectAddress, v: JBoolean) if v =>
        pow(2, a.integerValue.toDouble)
      case _ => 0
    }
    dIter.fold(0d)(_ + _).toLong
  }
  lazy val intValue = longValue.toInt
}

object IntegerBoolean1DUniverse{
  val empty = new IntegerBoolean1DUniverse
  def from(bitString: String) = new IntegerBoolean1DUniverse(bits = bitString.zipWithIndex.map{
    case ('0', i) => (Integer1DObjectAddress(i) -> JBoolean.FALSE)
    case ('1', i) => (Integer1DObjectAddress(i) -> JBoolean.TRUE)
    case c => throw new RuntimeException(s"""Cannot parse bitString. Found "$c". Expected "0" or "1"""")
  }.toMap)
}

case class Integer2DObjectAddress(
    val firstValue: Integer = 0,
    val secondValue: Integer = 0,
    val dimensions: List[Dimension[_]] =
      List(IntegerDimension("x"), IntegerDimension("y")))
    extends ObjectAddress {

  override val values =
    List(IntegerValue(firstValue), IntegerValue(firstValue))

  override def withValues[A <: ObjectAddress](
      values: List[DimensionValue[_]]): A =
    Integer2DObjectAddress(values(0).asInstanceOf[Integer],
                           values(1).asInstanceOf[Integer],
                           dimensions).asInstanceOf[A]

}

class Integer2DUniverse[V <: Comparable[_]](
    override val name: String = "Integer2DUniverse",
    val firstDimensionName: String = "x",
    val secondDimensionName: String = "y",
    override val objects: Map[Integer2DObjectAddress, _ <: V] = Map())
    extends Universe[Integer2DObjectAddress, V] {

  override val dimensions = List(IntegerDimension(firstDimensionName),
                                 IntegerDimension(secondDimensionName))

  override def withObjects(
      objects: Map[Integer2DObjectAddress, V]): Integer2DUniverse[V] = {
    new Integer2DUniverse(name,
                          firstDimensionName,
                          secondDimensionName,
                          objects)
  }

}

case class Integer3DObjectAddress(val firstValue: Integer = 0,
                                  val secondValue: Integer = 0,
                                  val thirdValue: Integer = 0,
                                  val dimensions: List[Dimension[_]] = List(
                                    IntegerDimension("x"),
                                    IntegerDimension("y"),
                                    IntegerDimension("z")))
    extends ObjectAddress {

  override val values = List(IntegerValue(firstValue),
                             IntegerValue(firstValue),
                             IntegerValue(thirdValue))

  override def withValues[A <: ObjectAddress](
      values: List[DimensionValue[_]]): A =
    Integer3DObjectAddress(values(0).asInstanceOf[Integer],
                           values(1).asInstanceOf[Integer],
                           values(2).asInstanceOf[Integer],
                           dimensions).asInstanceOf[A]

}

class Integer3DUniverse[V <: Comparable[_]](
    override val name: String = "Integer3DUniverse",
    val firstDimensionName: String = "x",
    val secondDimensionName: String = "y",
    val thirdDimensionName: String = "z",
    override val objects: Map[Integer3DObjectAddress, V] = Map())
    extends Universe[Integer3DObjectAddress, V] {

  override val dimensions = List(IntegerDimension(firstDimensionName),
                                 IntegerDimension(secondDimensionName),
                                 IntegerDimension(thirdDimensionName))

  override def withObjects(
      objects: Map[Integer3DObjectAddress, V]): Integer3DUniverse[V] = {
    new Integer3DUniverse(name,
                          firstDimensionName,
                          secondDimensionName,
                          thirdDimensionName,
                          objects)
  }

}

object IntegerImplicits {

  implicit def integer2IntegerValue(v: Integer): IntegerValue = IntegerValue(v)

  implicit def int2DimensionValue(v: Int): DimensionValue[Integer] =
    integer2IntegerValue(v)

}
