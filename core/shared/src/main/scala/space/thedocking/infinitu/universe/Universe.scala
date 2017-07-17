package space.thedocking.infinitu.universe

import space.thedocking.infinitu.dimension._

import scala.collection.immutable.SortedSet

trait ObjectAddress {

  val dimensions: List[Dimension[_]]

  val values: List[DimensionValue[_]]

  lazy val isValid = dimensions.zip(values).forall { case (d, v) =>
    d.asInstanceOf[Dimension[Comparable[_]]]
      .accept(v.asInstanceOf[DimensionValue[Comparable[_]]])
  }

  def valueAt[V <: Comparable[_]](d: Dimension[V]): DimensionValue[V] =
    values(dimensions.indexOf(d)).asInstanceOf[DimensionValue[V]]

  def updated[A <: ObjectAddress](valueIndex: Int, v: DimensionValue[_]) =
    withValues[A](values.updated(valueIndex, v))

  def withValues[A <: ObjectAddress](values: List[DimensionValue[_]]): A
}

trait Universe[A <: ObjectAddress, V <: Comparable[_]] {

  val name: String = "Universe"

  val dimensions: List[Dimension[_]]

  val keysByDimension = Map[Dimension[_], SortedSet[A]]()

  //TODO replace by proper access methods
  val objects: Map[A, V]

  def acceptValueOnAddress(address: A, value: V): Boolean = {
    address.values.zip(dimensions).forall {
      case (dimensionValue: DimensionValue[Any], dimension: Dimension[Any]) =>
        dimension.accept(dimensionValue)
    }
  }

  def validate(address: A, value: V) {
    require(
      acceptValueOnAddress(address, value),
      s"This universe does not accept the value $value in the address $address")
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
    val sortedAddresses =
      keys.toList.sortWith(AddressesByDimension(dimension).gt)
    val readdressedObjects = sortedAddresses
      .zip(sortedAddresses.reverse)
      .map {
        case (original, inverted) =>
          inverted -> objects(original)
      }
      .toMap
    withObjects(readdressedObjects)
  }

  def keys: Iterable[A] = objects.keys

  def values: Iterable[V] = objects.values

  def toIterable: Iterable[(A, V)] = objects.toIterable
  
  def size: Int = objects.size

  def map[B](f: ((A, V)) => B) = objects.map(f)

}

trait InvalidAddresses[A <: ObjectAddress] { self: Universe[_, _] =>

  def invalidAddresses: List[A] = Nil

  def validate(address: A, value: Any) {
    require(acceptAddress(address),
            s"The address $address is invalid in this universe")
  }

  def acceptAddress(address: A): Boolean = {
    address.values.zip(dimensions).forall {
      case (dimensionValue: DimensionValue[Any], dimension: Dimension[Any]) =>
        dimension.accept(dimensionValue)
    }
  }

}
