package space.thedocking.infinitu.obj

import space.thedocking.infinitu.dimension._
import space.thedocking.infinitu.integer.IntegerIntervalDimension
import space.thedocking.infinitu.integer.IntegerImplicits._
import space.thedocking.infinitu.universe._
import space.thedocking.infinitu.integer.IntegerValue

object ObjectValue {
  def apply[V](value: V): ObjectValue[V] =
    ObjectValue(Option(value), Seq(value))
}

case class ObjectValue[V](override val value: Option[V] = None,
                          val allValues: Seq[V] = Nil)
    extends DiscreteDimensionValue[Option[V]] {

  def isCompatible(value: Option[V] = None): Boolean = {
    //TODO refactor into a violation list...
    value.isEmpty || allValues.contains(value.get)
  }

  def checkRequirements(value: Option[V] = None) =
    require(isCompatible(value))

  checkRequirements()

  val indexDimension = new IntegerIntervalDimension(
    name = "object value indexes",
    minValue = 0,
    maxValue = allValues.size - 1)

  private def indexes(other: DimensionValue[_]) = {
    val otherIndex = IntegerValue {
      val v: Integer = other match {
        case IntegerValue(i) if i < allValues.size => i
        case ObjectValue(o: Option[V], _) if isCompatible(o) =>
          allValues.indexOf(o.get)
        case _ =>
          throw new RuntimeException(
            s"The current implementation is not ready to do $value minus $other")
      }
      v
    }
    val thisIndex = IntegerValue(value.map(allValues.indexOf(_)).getOrElse(0))
    (thisIndex, otherIndex)
  }

  private def operate(
      operation: (DimensionValue[Integer], DimensionValue[_]) => DimensionValue[
        Integer],
      other: DimensionValue[_]) = {
    val idx = indexes(other)
    copy(value = Option[V](allValues(operation.apply(idx._1, idx._2).value)))
  }

  //TODO general refactoring moving operators to trait...
  override def plus(other: DimensionValue[_]): ObjectValue[V] =
    operate(indexDimension.plus, other)

  override def minus(other: DimensionValue[_]): ObjectValue[V] =
    operate(indexDimension.minus, other)

  override lazy val minValue: DimensionValue[Option[V]] = copy(
    value = allValues.headOption)

  override lazy val maxValue: DimensionValue[Option[V]] = copy(
    value = allValues.reverse.headOption)

  override def next: DimensionValue[Option[V]] = plus(1)

  override def previous: DimensionValue[Option[V]] = minus(1)

}

object ObjectImplicits {

  implicit def object2ObjectValue[V](v: V): ObjectValue[V] = ObjectValue(v)

  implicit def objectSeq2ObjectValueSeq[V](v: Seq[V]): Seq[ObjectValue[V]] =
    v.map(object2ObjectValue)

}
