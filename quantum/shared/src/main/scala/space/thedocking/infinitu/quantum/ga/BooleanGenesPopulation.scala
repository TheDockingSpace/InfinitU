package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.universe.Universe

class BooleanGenesPopulation(
    override val individuals: Universe[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    extends Population[Integer1DObjectAddress, BooleanGenesChromosome] {

  override def evolve(times: Int) = ???
  override def newGeneration      = ???

}
