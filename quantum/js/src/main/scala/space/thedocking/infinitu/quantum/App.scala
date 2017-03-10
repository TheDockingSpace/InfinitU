package space.thedocking.infinitu.quantum

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import space.thedocking.infinitu.quantum.QuantumState._
import space.thedocking.infinitu.quantum.Plotter._
import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.dimension.DimensionValue

object App extends JSApp {

  val booleanCollapser: Collapser[Collapsable[BooleanValue, BooleanValue],
                                  BooleanValue] =
    new RandomFiniteSuperpositionCollapser

  def main(): Unit = {
    println("Hello quantum!")
    document.body.appendChild {
      document.createTextNode {
        s"The cat is ${if (BooleanSuperposition().collapse(booleanCollapser).value.value) {
          "alive! :)"
        } else {
          "dead. :("
        }}"
      }
    }
  }
}
