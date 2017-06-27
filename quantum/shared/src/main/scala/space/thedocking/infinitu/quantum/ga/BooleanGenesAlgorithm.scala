package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.{Integer1DObjectAddress, IntegerValue}

import scala.annotation.tailrec

class BooleanGenesAlgorithm
    extends Algorithm[Integer1DObjectAddress,
                      BooleanGenesChromosome,
                      IntegerValue] {

  override def evolve(population: Population[Integer1DObjectAddress,
                                             BooleanGenesChromosome,
                                             IntegerValue],
                      parameters: GenerationParameters[Integer1DObjectAddress,
                                                       BooleanGenesChromosome,
                                                       IntegerValue],
                      times: Int): Stream[Population[Integer1DObjectAddress,
                                                  BooleanGenesChromosome,
                                                  IntegerValue]] = {
    @tailrec
    def generate(acc: Stream[Population[Integer1DObjectAddress,
      BooleanGenesChromosome,
      IntegerValue]], times: Int): Stream[Population[Integer1DObjectAddress,
      BooleanGenesChromosome,
      IntegerValue]] = {
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

  override def evolve(population: Population[Integer1DObjectAddress,
                                             BooleanGenesChromosome,
                                             IntegerValue],
                      parameters: GenerationParameters[Integer1DObjectAddress,
                                                       BooleanGenesChromosome,
                                                       IntegerValue])
    : Population[Integer1DObjectAddress,
                 BooleanGenesChromosome,
                 IntegerValue] = {
    val analyzedPopulation: BooleanGenesPopulation = parameters.fitness match {
      case Left(individualFitness) =>
        calculateIndividual(individualFitness, population)
      case Right(relativeFitness) =>
        calculateRelative(
          relativeFitness,
          calculateIndividual(relativeFitness.individualFitness, population))
    }
    analyzedPopulation
  }

  def calculateIndividual(
      individualFitness: IndividualFitness[BooleanGenesChromosome,
                                           IntegerValue],
      population: Population[Integer1DObjectAddress,
                             BooleanGenesChromosome,
                             IntegerValue]): BooleanGenesPopulation = {
    val analyzedPopulation = population.individuals.map {
      case (address, chromosome) =>
        (address -> individualFitness.calculate(chromosome))
    }
    BooleanGenesPopulation(population.individuals, analyzedPopulation.toMap)
  }

  def calculateRelative(
      relativeFitness: RelativeFitness[BooleanGenesChromosome,
                                       IntegerValue,
                                       Population[Integer1DObjectAddress,
                                                  BooleanGenesChromosome,
                                                  IntegerValue]],
      analyzedPopulation: Population[Integer1DObjectAddress,
                                     BooleanGenesChromosome,
                                     IntegerValue]): BooleanGenesPopulation = {
    val reanalyzedPopulation = analyzedPopulation.map {
      case (address, chromosome) =>
        (address, relativeFitness.calculate(chromosome, analyzedPopulation))
    }
    BooleanGenesPopulation(analyzedPopulation.individuals,
                           reanalyzedPopulation.toMap)
  }

}
