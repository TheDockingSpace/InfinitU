package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.{ Integer1DObjectAddress, IntegerValue }
import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.integer.Integer1DUniverse

case class BooleanGenesPopulation(
  override val individuals: BooleanGenesIndividuals,
  val fitnessValues: Map[Integer1DObjectAddress, IntegerValue] = Map.empty)
    extends Population[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue] {

  override def fitnessValue(individualAddress: Integer1DObjectAddress): IntegerValue = fitnessValues(individualAddress)

}

object BooleanGenesPopulation {
  def apply(populationSize: Integer, chromosomeSize: Integer): BooleanGenesPopulation = {
    BooleanGenesPopulation(BooleanGenesIndividuals(individuals = (for (individual <- 0 until populationSize) yield (Integer1DObjectAddress(individual) -> BooleanGenesChromosome(chromosomeSize))).toMap))
  }
}

case class BooleanGenesIndividuals(
  override val name: String = "BooleanGenesIndividuals",
  override val dimensionName: String = "individual",
  val individuals: Map[Integer1DObjectAddress, BooleanGenesChromosome] = Map.empty)
    //TODO think about helper to translate 1d to 2d (individual x chromosome)
    //XXX why overloading objects leads to polymorphic issue?
    extends Integer1DUniverse[BooleanGenesChromosome](objects = individuals)
