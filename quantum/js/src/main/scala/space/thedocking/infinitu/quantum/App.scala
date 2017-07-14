package space.thedocking.infinitu.quantum

import org.scalajs.dom
import org.scalajs.dom.document
import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.quantum.QuantumState._

import scala.scalajs.js.JSApp

object App extends JSApp {

  def main(args: Array[String]): Unit = main()

  def main(): Unit = {
    println("Hello quantum!")
    document.body.appendChild {
      document.createTextNode {
        //true.collapse is a shortcut to
        //  val booleanCollapser: Collapser[Collapsable[BooleanValue, BooleanValue],
        //                                  BooleanValue] =
        //    new RandomFiniteSuperpositionCollapser
        //  BooleanSuperposition().collapse(booleanCollapser).value.value
        s"The cat is ${if (Boolean.collapse) {
          "alive! :)"
        } else {
          "dead. :("
        }}"
      }
    }
  }
}
