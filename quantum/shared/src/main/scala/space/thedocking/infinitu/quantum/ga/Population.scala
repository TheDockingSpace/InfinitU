package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.universe.ObjectAddress
import space.thedocking.infinitu.dimension.DiscreteDimensionValue

trait Fitness[C <: Chromosome[_, _]]

trait IndividualFitness[C <: Chromosome[_, _], V <: DiscreteDimensionValue[_]]
    extends Fitness[C] {
  def calculate(individual: C): V
}

trait RelativeFitness[C <: Chromosome[_, _],
                      P <: Population[_, C],
                      V <: DiscreteDimensionValue[_]]
    extends Fitness[C] {
  def calculate(individuals: C, population: P): V
}

trait ChromosomeSelection[C <: Chromosome[_, _]] {
  def apply[U <: Universe[_, C]](individuals: U): C
}

trait ChromosomeGroupSelection[U <: Universe[_, _]] {
  def apply(individuals: U): U
}

trait Population[A <: ObjectAddress, C <: Chromosome[A, _]] {
  def individuals: Universe[A, C]
  def evolve(times: Int): Seq[AnalyzedPopulation[A, C]]
}

trait AnalyzedPopulation[A <: ObjectAddress, C <: Chromosome[A, _]]
    extends Population[A, C] {
  def newGeneration[V <: DiscreteDimensionValue[_]](
      fitness: Either[IndividualFitness[C, V],
                      RelativeFitness[C, Population[A, C], V]],
      //TODO support multi-individual crossover
      pairSelection: ChromosomeSelection[C],
      crossover: ChromosomeOperation[C],
      mutagen: Mutagen[C],
      surviverSelection: ChromosomeGroupSelection[
        _ <: Universe[_ <: A, _ <: C]]): AnalyzedPopulation[A, C]
}
