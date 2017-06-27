package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.IntegerValue
import space.thedocking.infinitu.integer.Integer1DObjectAddress

trait BooleanGenesAlgorithm extends Algorithm[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue] {

  override def evolve(population: Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue],
    parameters: GenerationParameters[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue],
    times: Int): Seq[Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue]] = {
    for (generation <- 1 to times) {}
    ???
  }

  override def evolve(population: Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue],
    parameters: GenerationParameters[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue]): Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue] = {
    val analyzedPopulation = parameters.fitness match {
      case Left(individualFitness) => calculateIndividual(individualFitness, population)
      case Right(relativeFitness) => calculateRelative(relativeFitness, calculateIndividual(relativeFitness.individualFitness, population))
    }
    analyzedPopulation
  }

  def calculateIndividual(individualFitness: IndividualFitness[BooleanGenesChromosome, IntegerValue], 
      population: Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue]): BooleanGenesPopulation = {
    val analyzedPopulation = population.individuals.map {
      case (address, chromosome) =>
        (address -> individualFitness.calculate(chromosome))
    }
    BooleanGenesPopulation(population.individuals, analyzedPopulation.toMap)
  }

  def calculateRelative(relativeFitness: RelativeFitness[BooleanGenesChromosome, IntegerValue, BooleanGenesPopulation],
      analyzedPopulation: BooleanGenesPopulation) = {
    analyzedPopulation.map {
      case (address, chromosome) =>
        (address, relativeFitness.calculate(chromosome, analyzedPopulation))
    }
  }

}