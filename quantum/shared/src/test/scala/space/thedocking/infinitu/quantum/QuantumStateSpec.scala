package space.thedocking.infinitu.quantum

import org.junit.runner.RunWith
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import space.thedocking.infinitu.quantum.QuantumState._
import space.thedocking.infinitu.quantum.Plotter._
import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.dimension.DimensionValue

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

    "plot a result" in {
      val times = 10000
      val delta = 2 * (times / 100)
      val lines = BooleanSuperposition().collapse(booleanCollapser, times)
      println(plot(lines).mkString("\n"))
      val total = lines.map(_._2).sum
      total must beEqualTo(times)
      lines(CollapsedTrueValue) must beCloseTo(lines(CollapsedFalseValue),
                                               delta)
    }

    "plot an engangled result" in {
      val times = 10000
      val delta = 2 * (times / 100)
      val gate = CNotGate((BooleanSuperposition(), BooleanSuperposition()),
                          (booleanCollapser, booleanCollapser))
      val lines = gate.collapse(entanglementCollapser, times)
      println(plot(lines).mkString("\n"))
      val total = lines.map(_._2).sum
      total must beEqualTo(times)
    }

  }

}
