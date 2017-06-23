package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.dimension.DiscreteDimensionValue

class BooleanGenesPopulation(
    override val individuals: Universe[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    extends Population[Integer1DObjectAddress, BooleanGenesChromosome] {

  override def evolve(parameters: GenerationParameters[Integer1DObjectAddress,
                                                       BooleanGenesChromosome],
                      times: Int) = {
    for (generation <- 1 to times) {}
    ???
  }

}

case class BooleanGenesAnalyzedPopulation(
    override val individuals: Universe[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    extends BooleanGenesPopulation(individuals)
    with AnalyzedPopulation[Integer1DObjectAddress, BooleanGenesChromosome] {

  override def newGeneration(
      parameters: GenerationParameters[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    : AnalyzedPopulation[Integer1DObjectAddress, BooleanGenesChromosome] = ???

}
