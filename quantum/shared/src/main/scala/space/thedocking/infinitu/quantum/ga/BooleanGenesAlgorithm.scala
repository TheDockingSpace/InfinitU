package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.{Integer1DObjectAddress, IntegerValue}

import scala.annotation.tailrec

case class BooleanGenesAlgorithm()
    extends Algorithm[Integer1DObjectAddress,
                      BooleanGenesChromosome,
                      IntegerValue,
                      BooleanGenesPopulation] {

  override def evolve(population: BooleanGenesPopulation,
                      parameters: GenerationParameters[Integer1DObjectAddress,
                                                       BooleanGenesChromosome,
                                                       IntegerValue,
                                                       BooleanGenesPopulation],
                      times: Int): Stream[BooleanGenesPopulation] = {
    @tailrec
    def generate(acc: Stream[BooleanGenesPopulation], times: Int): Stream[BooleanGenesPopulation] = {
      times match {
        case t if t < 1 =>
          Stream.empty
        case _ =>
          val nextGeneration = evolve(population, parameters)
          generate(nextGeneration #:: acc,
            times - 1)
      }
    }
    generate(Stream.empty, times)
  }

  override def evolve(population: BooleanGenesPopulation,
                      parameters: GenerationParameters[Integer1DObjectAddress,
                                                       BooleanGenesChromosome,
                                                       IntegerValue,
                                                       BooleanGenesPopulation])
    : BooleanGenesPopulation = {
    val analyzedPopulation: BooleanGenesPopulation = parameters.fitness match {
      case individualFitness : IndividualFitness[BooleanGenesChromosome, IntegerValue] =>
        calculateIndividual(individualFitness, population)
      case relativeFitness: RelativeFitness[BooleanGenesChromosome, IntegerValue, BooleanGenesPopulation] =>
        calculateRelative(
          relativeFitness,
          calculateIndividual(relativeFitness.individualFitness, population))
    }
    analyzedPopulation
  }

  def calculateIndividual(
      individualFitness: IndividualFitness[BooleanGenesChromosome,
                                           IntegerValue],
      population: BooleanGenesPopulation): BooleanGenesPopulation = {
    val analyzedPopulation = population.individuals.map {
      case (address, chromosome) =>
        (address -> individualFitness.calculate(chromosome))
    }
    BooleanGenesPopulation(population.individuals, analyzedPopulation.toMap)
  }

  def calculateRelative(
      relativeFitness: RelativeFitness[BooleanGenesChromosome,
                                       IntegerValue,
                                       BooleanGenesPopulation],
      analyzedPopulation: BooleanGenesPopulation): BooleanGenesPopulation = {
    val reanalyzedPopulation = analyzedPopulation.map {
      case (address, chromosome) =>
        (address, relativeFitness.calculate(chromosome, analyzedPopulation))
    }
    BooleanGenesPopulation(analyzedPopulation.individuals,
                           reanalyzedPopulation.toMap)
  }

}
