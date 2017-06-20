package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.Integer1DObjectAddress
import java.lang.{Boolean => JBoolean}
import space.thedocking.infinitu.universe.Universe

case class BooleanGenesChromosome(
    override val genes: Universe[Integer1DObjectAddress, JBoolean])
    extends Chromosome[Integer1DObjectAddress, JBoolean] {

  override def crossover(other: Chromosome[Integer1DObjectAddress, JBoolean]) =
    ???
  override def mutate = ???
  override def compareTo(other: Chromosome[Integer1DObjectAddress, JBoolean]) =
    ???

}
