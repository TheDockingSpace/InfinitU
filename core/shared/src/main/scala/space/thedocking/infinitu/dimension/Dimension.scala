package space.thedocking.infinitu.dimension

import scala.util.Random

//TODO bake finiteness in values and dimensions?
abstract sealed trait Finiteness[V] {

  val isDimensionFinite = false

  def randomValue: V

}

trait Infinite[V <: DimensionValue[_]] extends Finiteness[V]

trait Finite[V <: DimensionValue[_]] extends Finiteness[V] {

  override val isDimensionFinite = true

  def allValues: Seq[V]

  override def randomValue: V = {
    val values = allValues
    require(
      values.size > 0,
      "Need at least one value on the dimension to be able to get it randomly :)")
    //TODO replace by logging framework
    if (values.size == 1) {
      val result = values(0)
      println(s"The only known value in dimension $this was ${values(0)}")
      result
    } else {
      val reminder = values.drop(Random.nextInt(values.size))
      reminder.head
    }
  }

}

trait DimensionValue[V] extends Ordered[DimensionValue[V]] {

  val value: V

  def minus(other: DimensionValue[_]): DimensionValue[V]

  def plus(other: DimensionValue[_]): DimensionValue[V]

  val minValue: DimensionValue[V]

  val maxValue: DimensionValue[V]

  lazy val isMinValue: Boolean = this.equals(minValue)

  lazy val isMaxValue: Boolean = this.equals(maxValue)

  override def compare(that: DimensionValue[V]): Int = {
    val aVal = this.value.asInstanceOf[Comparable[Any]]
    val bVal = that.value.asInstanceOf[Comparable[Any]]
    aVal.compareTo(bVal)
  }

}

trait Negatable[V <: DimensionValue[_]] {
  def negate: V
}

trait DiscreteDimensionValue[V] extends DimensionValue[V] {

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

  override def toString =
    String.format(include.template, left.value.toString, right.value.toString)

}

trait DiscreteDimensionInterval[V <: Comparable[_]]
    extends DimensionInterval[V] {

  def next: DimensionInterval[V]

  def previous: DimensionInterval[V]

}

trait Dimension[V <: Comparable[_]] {

  val name: String

  val origin: DimensionValue[V]

  def accept(someValue: DimensionValue[V]) =
    someValue.value.getClass.equals(origin.value.getClass)

  def minus(value: DimensionValue[V],
            other: DimensionValue[_]): DimensionValue[V] = value.minus(other)

  def plus(value: DimensionValue[V],
           other: DimensionValue[_]): DimensionValue[V] = value.plus(other)

  val minValue: DimensionValue[V]

  val maxValue: DimensionValue[V]

  def isMinValue(value: DimensionValue[_]): Boolean = value.equals(minValue)

  def isMaxValue(value: DimensionValue[_]): Boolean = value.equals(maxValue)

  def compare(value: DimensionValue[V], other: DimensionValue[V]): Int =
    value.compare(other)

  override def toString = name

}

trait DiscreteDimension[V <: Comparable[_]] extends Dimension[V] {

  override val origin: DiscreteDimensionValue[V]

}
