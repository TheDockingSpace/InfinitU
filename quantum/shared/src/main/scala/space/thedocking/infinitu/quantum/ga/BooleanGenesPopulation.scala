package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.dimension.DiscreteDimensionValue
import space.thedocking.infinitu.integer.IntegerValue

class BooleanGenesPopulation(
    override val individuals: Universe[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    extends Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue] {

  override def evolve(parameters: GenerationParameters[Integer1DObjectAddress,
                                                       BooleanGenesChromosome,
                                                       IntegerValue],
                      times: Int): Seq[
    AnalyzedPopulation[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue]] = {
    for (generation <- 1 to times) {}
    ???
  }

}

case class BooleanGenesAnalyzedPopulation(
    override val individuals: Universe[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    extends BooleanGenesPopulation(individuals)
    with AnalyzedPopulation[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue] {

  override def newGeneration(
      parameters: GenerationParameters[Integer1DObjectAddress,
                                       BooleanGenesChromosome,
                                       IntegerValue])
    : AnalyzedPopulation[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue] = {
    parameters.fitness match {
      case Left(individualFitness) => ???
      case Right(relativeFitness)=> ???
      ???
    }
    individuals.toIterable.map{case (address, individual) =>
      
    }
    ???
  }

}
