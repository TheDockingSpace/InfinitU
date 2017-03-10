package space.thedocking.infinitu.quantum

import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.dimension.DimensionValue
import space.thedocking.infinitu.dimension.Finite
import space.thedocking.infinitu.bool.TrueValue
import space.thedocking.infinitu.bool.FalseValue
import space.thedocking.infinitu.dimension.Finiteness

sealed trait QuantumState {

  val isSuperposition = false

  val isCollapsed = false

  val isEntanglement = false

  //TODO would isEntangled be useful?

}

trait CollapsedValue[V] extends QuantumState {

  val value: V

  override val isCollapsed = true

}

trait Collapser[-I <: Collapsable[_, _], O] {

  def collapse(state: I): CollapsedValue[O]

  def collapse(state: I, times: Int): Map[CollapsedValue[O], Int] = {
    val values = for (i <- 1 to times) yield collapse(state)
    values.groupBy(identity(_)).mapValues(_.size).toMap
  }

}

trait Collapsable[I, O] {

  def collapse(
      collapser: Collapser[_ <: Collapsable[I, O], O]): CollapsedValue[O] =
    //TODO kill this asInstanceOf
    collapser.asInstanceOf[Collapser[Collapsable[I, O], O]].collapse(this)

  def collapse(collapser: Collapser[Collapsable[I, O], O],
               times: Int): Map[CollapsedValue[O], Int] =
    collapser.collapse(this, times)

  def collapsed(value: I): CollapsedValue[O]

}

trait Superposition[V] extends QuantumState with Collapsable[V, V] {
  self: Finiteness[V] =>

  override val isSuperposition = true

}

trait Entanglement[SI, SO, EI, EO]
    extends QuantumState
    with Collapsable[List[EI], List[EO]] {

  override val isEntanglement = true

  val entangledStates: List[_ <: Collapsable[SI, SI]]

  val entangledCollapsers: List[_ <: Collapser[_ <: Collapsable[SI, SI], SO]]

}

trait HomogeneousEntanglement[V] extends Entanglement[V, V, V, V]

class RandomFiniteSuperpositionCollapser[
    C <: Collapsable[V, V], V <: DimensionValue[_]]
    extends Collapser[C, V] {

  override def collapse(superposition: C): CollapsedValue[V] = {
    //TODO kill this asInstanceOf
    superposition.collapsed(
      superposition.asInstanceOf[Finiteness[V]].randomValue)
  }

}

object Plotter {

  implicit object ComparableOrdering extends Ordering[Comparable[_]] {
    def compare(first: Comparable[_], second: Comparable[_]): Int =
      //TODO kill this asInstanceOf
      first.compareTo(second.asInstanceOf)
  }

  //TODO make the collapsed value responsible for pretty printing
  private def prettyEntry(entry: (CollapsedValue[_], Int)): (String, Int) =
    entry match {
      case (k, v) if (k.value.isInstanceOf[DimensionValue[_]]) =>
        (k.value.asInstanceOf[DimensionValue[_]].value.toString, v)
      case (k, v) =>
        (k.value.toString, v)
    }

  def plot[V](values: Map[CollapsedValue[V], Int]): Seq[String] = {
    val maxCount   = values.map(_._2).max
    val totalCount = values.map(_._2).sum

    val raw: Map[String, Int] = values.map(prettyEntry)
    val maxLabel              = raw.map(_._1.size).max
    val sorted                = raw.toSeq.sortBy(_._1)
    val maxBar                = 20
    for {
      (k, v) <- sorted
      val label   = f"$k (${100D * v / totalCount}%3.2f%%)"
      val padding = maxLabel + (label.size - k.size)
    } yield
      s"${label
        .padTo(padding, " ")
        .mkString("")} ${"=" * (maxBar * v / maxCount)}"
  }

}

object QuantumState {

  implicit def booleanToQS(value: Boolean): CollapsedBooleanValue =
    if (value) CollapsedTrueValue else CollapsedFalseValue

}

//Boolean specific....

trait CollapsedBooleanValue extends CollapsedValue[BooleanValue]

object CollapsedTrueValue extends CollapsedBooleanValue {
  override val value: BooleanValue = TrueValue
}

object CollapsedFalseValue extends CollapsedBooleanValue {
  override val value: BooleanValue = FalseValue
}

case class CollapsedBooleanValues(override val value: List[BooleanValue])
    extends CollapsedValue[List[BooleanValue]]

case class BooleanSuperposition()
    extends Superposition[BooleanValue]
    with Finite[BooleanValue] {

  override val allValues: Seq[BooleanValue] = Seq(FalseValue, TrueValue)

  override def collapsed(value: BooleanValue) =
    if (value.value) CollapsedTrueValue else CollapsedFalseValue

}

case class BooleanEntanglementCollapser()
    extends Collapser[Collapsable[List[BooleanValue], List[BooleanValue]],
                      List[BooleanValue]] {

  override def collapse(
      value: Collapsable[List[BooleanValue], List[BooleanValue]])
    : CollapsedValue[List[BooleanValue]] = {
    val entanglement =
      value.asInstanceOf[HomogeneousEntanglement[BooleanValue]]
    val entangled = entanglement.entangledCollapsers
      .zip(entanglement.entangledStates)
      .map {
        case (collapser, state) =>
          state.collapse(collapser).value
      }
      .toList
    entanglement.collapsed(entangled)
  }

}

case class CNotGate(
    states: Tuple2[BooleanSuperposition, BooleanSuperposition],
    collapsers: Tuple2[_ <: Collapser[BooleanSuperposition, BooleanValue],
                       _ <: Collapser[BooleanSuperposition, BooleanValue]])
    extends HomogeneousEntanglement[BooleanValue] {

  override val entangledStates = states._1 :: states._2 :: Nil

  override val entangledCollapsers =
    collapsers._1 :: collapsers._2 :: Nil

  override def collapsed(values: List[BooleanValue]) = {
    CollapsedBooleanValues(values match {
      case TrueValue :: other :: Nil => TrueValue :: other.negate :: Nil
      case _                         => values
    })
  }

}
