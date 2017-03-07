package space.thedocking.infinitu.dimension

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

  override def toString = name

}

trait DiscreteDimension[V <: Comparable[_]] extends Dimension[V] {

  override val origin: DiscreteDimensionValue[V]

}
