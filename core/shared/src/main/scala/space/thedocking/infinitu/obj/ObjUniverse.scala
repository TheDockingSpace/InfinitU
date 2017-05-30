package space.thedocking.infinitu.obj

import space.thedocking.infinitu.dimension._
import space.thedocking.infinitu.integer.IntegerIntervalDimension
import space.thedocking.infinitu.integer.IntegerImplicits._
import space.thedocking.infinitu.universe._

class ObjectValue[V](
    override val value: Option[V] = None,
                    val allValues: List[V] = Nil,
                    val index: Int = 0
) extends DiscreteDimensionValue[Option[V]] {

  def checkRequirements(value: Option[V] = None) =
  require(value.isEmpty || allValues.contains(value.get))

  checkRequirements()

  val indexes = new IntegerIntervalDimension(name = "object value indexes",
  minValue = 0,
  maxValue = allValues.size - 1)

  //TODO general refactoring moving operators to trait...
  override def plus(other: DimensionValue[_]): ObjectValue[V] = this

  override def minus(other: DimensionValue[_]): ObjectValue[V] = this

  override lazy val minValue: DimensionValue[Option[V]] = this

  override lazy val maxValue: DimensionValue[Option[V]] = this

  override def next: DimensionValue[Option[V]] = this

  override def previous: DimensionValue[Option[V]] = this

}
