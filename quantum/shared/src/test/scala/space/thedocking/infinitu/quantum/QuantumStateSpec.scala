package space.thedocking.infinitu.quantum

import org.junit.runner.RunWith
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.obj.ObjectImplicits._
import space.thedocking.infinitu.obj.ObjectValue
import space.thedocking.infinitu.quantum.Plotter._
import space.thedocking.infinitu.quantum.QuantumState._

@RunWith(classOf[JUnitRunner])
class QuantumStateSpec extends Specification with ShouldMatchers {

  val trueQS: QuantumState = true
  val booleanCollapser: Collapser[Collapsable[BooleanValue, BooleanValue],
                                  BooleanValue] =
    new RandomFiniteSuperpositionCollapser
  val entanglementCollapser: Collapser[
    Collapsable[List[BooleanValue], List[BooleanValue]],
    List[BooleanValue]] = BooleanEntanglementCollapser()

  "QuantumState" should {

    "plot a qubit result" in {
      val times = 10000
      val delta = 3 * (times / 100)
      val lines = BooleanSuperposition().collapse(booleanCollapser, times)
      println(plot(lines))
      val total = lines.map(_._2).sum
      total must beEqualTo(times)
      lines.getOrElse(CollapsedTrueValue, 0) must beCloseTo(
        lines.getOrElse(CollapsedFalseValue, 0),
        delta)
    }

    "plot a two qubit engangled result" in {
      val times = 10000
      val delta = 2 * (times / 100)
      val gate = CNotGate((BooleanSuperposition(), BooleanSuperposition()),
                          (booleanCollapser, booleanCollapser))
      val lines = gate.collapse(entanglementCollapser, times)
      println(plot(lines))
      val total = lines.map(_._2).sum
      total must beEqualTo(times)
    }

    sealed trait MockState {
      val label: String
      override def toString() = label
    }

    object YesState extends MockState {
      override val label = "Yes"
    }
    object NoState extends MockState {
      override val label = "No"
    }
    object MaybeState extends MockState {
      override val label = "Maybe"
    }

    def assertEquallyDistributed(values: Seq[_],
                                 lines: Map[_ <: CollapsedValue[_], Int],
                                 totalRuns: Int) = {
      val total = lines.map(_._2).sum
      total must beEqualTo(totalRuns)
      val reference = lines.values.head
      val delta     = 2 * (totalRuns / 100)
      forall(values) { v =>
        lines.keys.find { c =>
          v.equals(unwrapValue(c))
        }.isDefined aka s"value $v should be among the keys" must beTrue
      }
      forall(lines) {
        case (value, times) =>
          times must beCloseTo(reference, delta)
      }
    }

    //TODO add cases using the class directly, without using generics
    //TODO complete and test heterogeneous superposition...
    val objCollapser: Collapser[
      Collapsable[ObjectValue[MockState], ObjectValue[MockState]],
      ObjectValue[MockState]] =
      new RandomFiniteSuperpositionCollapser
    //TODO qudit entanglement example
    val objEntanglementCollapser: Collapser[
      Collapsable[List[ObjectValue[MockState]], List[ObjectValue[MockState]]],
      List[ObjectValue[MockState]]] = ObjectEntanglementCollapser()

    "plot a qudit result with sealed trait" in {
      val times  = 10000
      val values = Seq(YesState, NoState, MaybeState)
      val lines  = ObjectSuperposition(values).collapse(objCollapser, times)
      println(plot(lines))
      //TODO add some implicits to avoid the constructors
      //TODO example iterating over enum
      assertEquallyDistributed(values, lines, times)
    }

    val objectCollapser: Collapser[
      Collapsable[ObjectValue[Object], ObjectValue[Object]],
      ObjectValue[Object]] =
      new RandomFiniteSuperpositionCollapser

    "plot a qudit result with objects" in {
      val times = 10000
      val values =
        Seq("can" :: "we" :: "do" :: "it?" :: Nil, YesState, "WE CAN!!")
      val lines = ObjectSuperposition(values).collapse(objectCollapser, times)
      println(plot(lines, sortByKey = false))
      assertEquallyDistributed(values, lines, times)
    }

  }

}
