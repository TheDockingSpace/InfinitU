package space.thedocking.infinitu.integer

import space.thedocking.infinitu.dimension._
import space.thedocking.infinitu.universe._

case class IntegerValue(override val value: Integer = 0)
    extends DiscreteDimensionValue[Integer] {

  override def plus(other: DimensionValue[Integer]) =
    IntegerValue(value + other.value)

  override def minus(other: DimensionValue[Integer]) =
    IntegerValue(value - other.value)

  override def next = IntegerValue(value + 1)

  override def previous = IntegerValue(value - 1)

}

case class IntegerInterval(override val left: IntegerValue,
                           override val right: IntegerValue,
                           override val include: DimensionIntervalInclude =
                             DimensionIntervalInclude.Both)
    extends DiscreteDimensionInterval[Integer] {

  override def next =
    IntegerInterval(right,
                    right.plus(size),
                    if (include.right) DimensionIntervalInclude.Right
                    else DimensionIntervalInclude.Left)

  override def previous =
    IntegerInterval(left,
                    left.minus(size),
                    if (include.left) DimensionIntervalInclude.Left
                    else DimensionIntervalInclude.Right)

}

case class IntegerDimension(override val name: String)
    extends DiscreteDimension[Integer] {

  override val origin = IntegerValue(0)

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

  override def withObjects(objects: Map[Integer2DObjectAddress, V]) = {
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

  override def withObjects(objects: Map[Integer3DObjectAddress, V]) = {
    new Integer3DUniverse(name,
                          firstDimensionName,
                          secondDimensionName,
                          thirdDimensionName,
                          objects)
  }

}
