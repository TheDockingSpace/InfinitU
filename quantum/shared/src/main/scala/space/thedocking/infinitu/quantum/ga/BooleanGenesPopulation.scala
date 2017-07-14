package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.{ Integer1DObjectAddress, IntegerValue }
import space.thedocking.infinitu.universe.Universe

case class BooleanGenesPopulation(
  override val individuals: BooleanGenesIndividuals,
  val fitnessValues: Map[Integer1DObjectAddress, IntegerValue] = Map.empty)
    extends Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue] {

  override def fitnessValue(individualAddress: Integer1DObjectAddress): IntegerValue = fitnessValues(individualAddress)

}

object BooleanGenesPopulation {
  def apply(populationSize: Integer, chromosomeSize: Integer): BooleanGenesPopulation = {
    BooleanGenesPopulation(BooleanGenesIndividuals((for (individual <- 0 until populationSize) yield (Integer1DObjectAddress(individual) -> BooleanGenesChromosome(chromosomeSize))).toMap))
  }
}

case class BooleanGenesIndividuals(override val objects: Map[Integer1DObjectAddress, BooleanGenesChromosome]) extends Universe[Integer1DObjectAddress, BooleanGenesChromosome]
