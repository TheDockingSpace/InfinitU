package infinitu

import scala.collection.immutable.SortedSet
import scala.collection.{ mutable }

trait DimensionValue[V <: Comparable[_]] extends Ordered[DimensionValue[V]] {

  val value: V

  def minus(other: DimensionValue[V]): DimensionValue[V]

  def plus(other: DimensionValue[V]): DimensionValue[V]

  override def compare(that: DimensionValue[V]): Int = {
    val aVal = this.value.asInstanceOf[Comparable[Any]]
    val bVal = that.value.asInstanceOf[Comparable[Any]]
    aVal.compareTo(bVal)
  }

}

trait DiscreteDimensionValue[V <: Comparable[_]] extends DimensionValue[V] {

  def next: DimensionValue[V]

  def previous: DimensionValue[V]

}

sealed trait DimensionIntervalInclude {

  val left: Boolean

  val right: Boolean

  val template: String

}

object DimensionIntervalInclude {

  case object Both extends DimensionIntervalInclude {

    override val left = true

    override val right = true

    override val template = "[%s, %s]"

  }

  case object Left extends DimensionIntervalInclude {

    override val left = true

    override val right = false

    override val template = "[%s, %s["

  }

  case object Right extends DimensionIntervalInclude {

    override val left = false

    override val right = true

    override val template = "]%s, %s]"

  }

  case object None extends DimensionIntervalInclude {

    override val left = false

    override val right = false

    override val template = "]%s, %s["
  }

}

trait DimensionInterval[V <: Comparable[_]] {

  val left: DimensionValue[V]

  val right: DimensionValue[V]

  val include: DimensionIntervalInclude = DimensionIntervalInclude.Both

  def size = right.minus(left)

  override def toString = String.format(include.template, left.value.toString, right.value.toString)

}

trait DiscreteDimensionInterval[V <: Comparable[_]] extends DimensionInterval[V] {

  def next: DimensionInterval[V]

  def previous: DimensionInterval[V]

}

trait Dimension[V <: Comparable[_]] {

  val name: String

  val origin: DimensionValue[V]

  def accept(someValue: DimensionValue[V]) = someValue.value.getClass.equals(origin.value.getClass)

  override def toString = name

}

trait DiscreteDimension[V <: Comparable[_]] extends Dimension[V] {

  override val origin: DiscreteDimensionValue[V]

}

trait ObjectAddress {

  val dimensions: List[Dimension[_]]

  val values: List[DimensionValue[_]]

  for ((d, v) <- dimensions.zip(values)) {
    d.asInstanceOf[Dimension[Comparable[_]]].accept(v.asInstanceOf[DimensionValue[Comparable[_]]])
  }

  def valueAt[V <: Comparable[_]](d: Dimension[V]): DimensionValue[V] = values(dimensions.indexOf(d)).asInstanceOf[DimensionValue[V]]

  def updated[A <: ObjectAddress](valueIndex: Int, v: DimensionValue[_]) = withValues[A](values.updated(valueIndex, v))

  def withValues[A <: ObjectAddress](values: List[DimensionValue[_]]): A
}

trait Universe[A <: ObjectAddress, V <: Comparable[_]] {

  val name: String = "Universe"

  val dimensions: List[Dimension[_]]

  val keysByDimension = Map[Dimension[_], SortedSet[A]]()

  val objects: Map[A, V]

  def acceptValueOnAddress(address: A, value: V): Boolean = {
    address.values.zip(dimensions).forall {
      case (dimensionValue: DimensionValue[Any], dimension: Dimension[Any]) =>
        dimension.accept(dimensionValue)
    }
  }

  def validate(address: A, value: V) {
    require(acceptValueOnAddress(address, value), s"This universe does not accept the value $value in the address $address")
  }

  def put(address: A, value: V): Universe[A, V] = {
    validate(address, value)
    withObjects(objects + (address -> value))
  }

  def withObjects(objects: Map[A, V]): Universe[A, V]

  def get(address: A): Option[V] = objects.get(address)

  def apply(address: A) = objects(address)

  case class AddressesByDimension(d: Dimension[V]) extends Ordering[A] {
    def compare(a: A, b: A): Int = a.valueAt(d).compare(b.valueAt(d))
  }

  def invertAddress(dimension: Dimension[V]): Universe[A, V] = {
    val sortedAddresses = objects.keys.toList.sortWith(AddressesByDimension(dimension).gt)
    val readdressedObjects = sortedAddresses.zip(sortedAddresses.reverse).map {
      case (original: A, inverted: A) =>
        inverted -> objects(original)
    }.toMap
    withObjects(readdressedObjects)
  }

}

trait InvalidAddresses[A <: ObjectAddress] {
  self: Universe[_, _] =>

  def invalidAddresses: List[A] = Nil

  def validate(address: A, value: Any) {
    self.validate(address, value)
    require(acceptAddress(address), s"The address $address is invalid in this universe")
  }

  def acceptAddress(address: A): Boolean = {
    address.values.zip(dimensions).forall {
      case (dimensionValue: DimensionValue[Any], dimension: Dimension[Any]) =>
        dimension.accept(dimensionValue)
    }
  }

}

//Integer dimension

case class IntegerValue(override val value: Integer = 0) extends DiscreteDimensionValue[Integer] {

  override def plus(other: DimensionValue[Integer]) = IntegerValue(value + other.value)

  override def minus(other: DimensionValue[Integer]) = IntegerValue(value - other.value)

  override def next = IntegerValue(value + 1)

  override def previous = IntegerValue(value - 1)

}

case class IntegerInterval(override val left: IntegerValue, override val right: IntegerValue, override val include: DimensionIntervalInclude = DimensionIntervalInclude.Both)
    extends DiscreteDimensionInterval[Integer] {

  override def next = IntegerInterval(right, right.plus(size), if (include.right) DimensionIntervalInclude.Right else DimensionIntervalInclude.Left)

  override def previous = IntegerInterval(left, left.minus(size), if (include.left) DimensionIntervalInclude.Left else DimensionIntervalInclude.Right)

}

case class IntegerDimension(override val name: String) extends DiscreteDimension[Integer] {

  override val origin = IntegerValue(0)

}

case class IntegerTwoDObjectAddress(val firstValue: Integer = 0, val secondValue: Integer = 0, val dimensions: List[Dimension[_]] = List(IntegerDimension("x"), IntegerDimension("y"))) extends ObjectAddress {

  override val values = List(IntegerValue(firstValue), IntegerValue(firstValue))

  override def withValues[A <: ObjectAddress](values: List[DimensionValue[_]]): A = IntegerTwoDObjectAddress(values(0).asInstanceOf[Integer], values(1).asInstanceOf[Integer], dimensions).asInstanceOf[A]

}

class IntegerTwoDUniverse[V <: Comparable[_]](override val name: String = "IntegerTwoDUniverse", val firstDimensionName: String = "x", val secondDimensionName: String = "y", override val objects: Map[IntegerTwoDObjectAddress, _ <: V] = Map())
    extends Universe[IntegerTwoDObjectAddress, V] {

  override val dimensions = List(IntegerDimension(firstDimensionName), IntegerDimension(secondDimensionName))

  override def withObjects(objects: Map[IntegerTwoDObjectAddress, V]) = {
    new IntegerTwoDUniverse(name, firstDimensionName, secondDimensionName, objects)
  }

}

case class IntegerThreeDObjectAddress(val firstValue: Integer = 0, val secondValue: Integer = 0, val thirdValue: Integer = 0, val dimensions: List[Dimension[_]] = List(IntegerDimension("x"), IntegerDimension("y"), IntegerDimension("z"))) extends ObjectAddress {

  override val values = List(IntegerValue(firstValue), IntegerValue(firstValue), IntegerValue(thirdValue))

  override def withValues[A <: ObjectAddress](values: List[DimensionValue[_]]): A = IntegerThreeDObjectAddress(values(0).asInstanceOf[Integer], values(1).asInstanceOf[Integer], values(2).asInstanceOf[Integer], dimensions).asInstanceOf[A]

}

class IntegerThreeDUniverse[V <: Comparable[_]](override val name: String = "IntegerThreeDUniverse", val firstDimensionName: String = "x", val secondDimensionName: String = "y", val thirdDimensionName: String = "z", override val objects: Map[IntegerThreeDObjectAddress, V] = Map())
    extends Universe[IntegerThreeDObjectAddress, V] {

  override val dimensions = List(IntegerDimension(firstDimensionName), IntegerDimension(secondDimensionName), IntegerDimension(thirdDimensionName))

  override def withObjects(objects: Map[IntegerThreeDObjectAddress, V]) = {
    new IntegerThreeDUniverse(name, firstDimensionName, secondDimensionName, thirdDimensionName, objects)
  }

}
