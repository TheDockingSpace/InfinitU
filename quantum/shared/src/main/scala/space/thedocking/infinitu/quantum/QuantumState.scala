package space.thedocking.infinitu.quantum

import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.dimension.DimensionValue
import space.thedocking.infinitu.dimension.Finite
import space.thedocking.infinitu.bool.TrueValue
import space.thedocking.infinitu.bool.FalseValue
import space.thedocking.infinitu.dimension.Finiteness
import space.thedocking.infinitu.obj.ObjectValue

sealed trait QuantumState {

  def prettyValue: String

  val isSuperposition = false

  val isCollapsed = false

  val isEntanglement = false

  //TODO would isEntangled be useful?

}

trait CollapsedValue[V] extends QuantumState {

  val value: V

  override val isCollapsed = true

  override lazy val prettyValue = QuantumState.prettyValue(value)

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

  override val prettyValue = "S"

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

  private def prettyEntry(entry: (CollapsedValue[_], Int)): (String, Int) =
    entry match {
      case (k, v) =>
        (QuantumState.prettyValue(k), v)
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

  implicit def toCollapsed(value: Boolean): CollapsedBooleanValue =
    if (value) CollapsedTrueValue else CollapsedFalseValue

  implicit def toSuperposition(b: Boolean.type): BooleanSuperposition =
    BooleanSuperposition()

  def prettyValue(value: Any): String =
    value match {
      case q: QuantumState      => q.prettyValue
      case o: Option[_]         => prettyValue(o.getOrElse("?"))
      case v: DimensionValue[_] => prettyValue(v.value)
      case null                 => "!"
      case _                    => value.toString
    }

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
    extends CollapsedValue[List[BooleanValue]] {

  override lazy val prettyValue =
    value.map(v => if (v.value) "1" else "0").mkString("")

}

case class BooleanSuperposition()
    extends Superposition[BooleanValue]
    with Finite[BooleanValue] {

  override val allValues: Seq[BooleanValue] = Seq(FalseValue, TrueValue)

  override def collapsed(value: BooleanValue) =
    if (value.value) CollapsedTrueValue else CollapsedFalseValue

  //TODO This should go to a registry
  val defaultCollapser: Collapser[Collapsable[BooleanValue, BooleanValue],
                                  BooleanValue] =
    new RandomFiniteSuperpositionCollapser

  //TODO In the future, write a proper DSL with useful shortcuts and syntatic sugar
  def collapse: Boolean = this.collapse(defaultCollapser).value.value

  def collapse(times: Int): Map[Boolean, Int] =
    this.collapse(defaultCollapser, times).map {
      case (k, v) =>
        val b: Boolean = k.value.value
        (b, v)
    }

  def cnot(other: BooleanSuperposition) =
    CNotGate((this, other), (defaultCollapser, defaultCollapser))
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

  override val prettyValue = "CNOT"

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

//Object specific....

//TODO split redundant/shared logic and specific classes in different files and packages

object CollapsedObjectValue {
  def apply[V](value: V): CollapsedObjectValue[V] = CollapsedObjectValue(ObjectValue(value))
}

case class CollapsedObjectValue[V]( override val value: ObjectValue[V]) extends CollapsedValue[ObjectValue[V]]

case class ObjectSuperposition[V](override val allValues: Seq[ObjectValue[V]])
    extends Superposition[ObjectValue[V]]
    //TODO refactor to support infinite dimension superposition
    with Finite[ObjectValue[V]] {

  override def collapsed(value: ObjectValue[V]) = CollapsedObjectValue(value)

  //TODO This should go to a registry
  val defaultCollapser: Collapser[Collapsable[ObjectValue[V], ObjectValue[V]],
                                  ObjectValue[V]] =
    new RandomFiniteSuperpositionCollapser

  //TODO In the future, write a proper DSL with useful shortcuts and syntatic sugar
  def collapse: Option[V] = this.collapse(defaultCollapser).value.value

  def collapse(times: Int): Map[Option[V], Int] =
    this.collapse(defaultCollapser, times).map {
      case (k, v) =>
        val b: Option[V] = k.value.value
        (b, v)
    }

}

case class ObjectEntanglementCollapser[V]()
    extends Collapser[Collapsable[List[ObjectValue[V]], List[ObjectValue[V]]],
                      List[ObjectValue[V]]] {

  override def collapse(
      value: Collapsable[List[ObjectValue[V]], List[ObjectValue[V]]])
    : CollapsedValue[List[ObjectValue[V]]] = {
    val entanglement =
      value.asInstanceOf[HomogeneousEntanglement[ObjectValue[V]]]
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
