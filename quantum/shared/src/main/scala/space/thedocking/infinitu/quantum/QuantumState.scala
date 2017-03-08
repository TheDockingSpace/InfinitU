package space.thedocking.infinitu.quantum

import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.dimension.DimensionValue
import space.thedocking.infinitu.dimension.Finite
import space.thedocking.infinitu.bool.TrueValue
import space.thedocking.infinitu.bool.FalseValue
import space.thedocking.infinitu.dimension.Finiteness

sealed trait QuantumState[V] {

  val isSuperposition = false

  val isCollapsed = false

  def superposition: Superposition[V]

}

trait CollapsedValue[V] extends QuantumState[V] {

  val value: V

  override val isCollapsed = true

}

trait Collapser[V] {

  def collapse(superposition: Superposition[V]): CollapsedValue[V]

  def collapse(superposition: Superposition[V], times: Int): Map[CollapsedValue[V], Int]

}

trait Superposition[V] extends QuantumState[V] {
  this: Finiteness =>

  override val isSuperposition = true

  def collapse(collapser: Collapser[V]): CollapsedValue[V] = collapser.collapse(this)

  def collapse(collapser: Collapser[V], times: Int): Map[CollapsedValue[V], Int] = collapser.collapse(this, times)

  def collapsed(value: V): CollapsedValue[V]

  override def superposition: Superposition[V] = this

}

class RandomFiniteSuperpositionCollapser[V <: DimensionValue[_]] extends Collapser[V] {

  override def collapse(superposition: Superposition[V]): CollapsedValue[V] = {
    //TODO kill this asInstanceOf
    val values = superposition.asInstanceOf[Finite[V]].allValues
    superposition.collapsed(values.drop(scala.util.Random.nextInt(values.size)).head)
  }

  override def collapse(superposition: Superposition[V], times: Int): Map[CollapsedValue[V], Int] = {
    val values = for (i <- 1 to times) yield collapse(superposition)
    values.groupBy(identity(_)).mapValues(_.size).toMap
  }

}

//Boolean specific....

trait CollapsedBooleanValue extends CollapsedValue[BooleanValue] {
  override val superposition: Superposition[BooleanValue] = BooleanSuperposition
}

object CollapsedTrueValue extends CollapsedBooleanValue {
  override val value: BooleanValue = TrueValue
}

object CollapsedFalseValue extends CollapsedBooleanValue {
  override val value: BooleanValue = FalseValue
}

object BooleanSuperposition
    extends Superposition[BooleanValue]
    with Finite[BooleanValue] {

  override val allValues: Seq[BooleanValue] = Seq(FalseValue, TrueValue)

  override def collapsed(value: BooleanValue) = if (value.value) CollapsedTrueValue else CollapsedFalseValue

}

object Plotter {

  implicit object ComparableOrdering extends Ordering[Comparable[_]] {
    def compare(c1: Comparable[_], c2: Comparable[_]): Int = c1.compareTo(c2.asInstanceOf)
  }

  def plot[V <: DimensionValue[_ <: Comparable[_]]](values: Map[CollapsedValue[V], Int]): Seq[String] = {
    val maxCount = values.map(_._2).max
    val totalCount = values.map(_._2).sum
    val raw: Map[String, Int] = values.map {
      case (k, v) =>
        (f"${k.value.value.toString} (${100D*v/totalCount}%2.2f%%)", v)
    }
    val maxLabel = raw.map(_._1.size).max
    val sorted = raw.toSeq.sortBy(_._1)
    val maxBar = 20
    for ((k,v) <- sorted) yield s"${k.padTo(maxLabel, " ").mkString("")} ${"=" * (maxBar * v / maxCount)}"
  }

}

object QuantumState {

  implicit def booleanToQS(value: Boolean): CollapsedBooleanValue = if (value) CollapsedTrueValue else CollapsedFalseValue

}
