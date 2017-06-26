package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.universe.ObjectAddress
import space.thedocking.infinitu.dimension.DiscreteDimensionValue

trait Fitness[C <: Chromosome[_, _], F <: DiscreteDimensionValue[_]]

trait IndividualFitness[C <: Chromosome[_, _], F <: DiscreteDimensionValue[_]]
    extends Fitness[C, F] {
  def calculate(individual: C): F
}

trait RelativeFitness[C <: Chromosome[_, _],
                      F <: DiscreteDimensionValue[_],
                      P <: Population[_, C, F],
                      V <: DiscreteDimensionValue[_]]
    extends Fitness[C, F] {
  def individualFitness(): IndividualFitness[C, F]
  def calculate(individuals: C, population: P): V
}

trait ChromosomeSelection[C <: Chromosome[_, _]] {
  def apply[U <: Universe[_, C]](individuals: U): C
}

trait ChromosomeGroupSelection[U <: Universe[_, _]] {
  def apply(individuals: U): U
}

class GenerationParameters[A <: ObjectAddress, C <: Chromosome[A, _], F <: DiscreteDimensionValue[_]](
    val fitness: Either[
      IndividualFitness[C, _ <: DiscreteDimensionValue[F]],
      RelativeFitness[C, F, Population[A, C, F], _ <: DiscreteDimensionValue[F]]],
    //TODO support multi-individual crossover
    val pairSelection: ChromosomeSelection[C],
    val crossover: ChromosomeOperation[C],
    val mutagen: Mutagen[C],
    val surviverSelection: ChromosomeGroupSelection[
      _ <: Universe[_ <: A, _ <: C]]
)

trait Population[A <: ObjectAddress, C <: Chromosome[A, _], F <: DiscreteDimensionValue[_]] {
  def individuals: Universe[A, C]
  def evolve(parameters: GenerationParameters[A, C, F],
             times: Int): Seq[AnalyzedPopulation[A, C, F]]
}

trait AnalyzedPopulation[A <: ObjectAddress, C <: Chromosome[A, _], F <: DiscreteDimensionValue[_]]
    extends Population[A, C, F] {
  def newGeneration(
      parameters: GenerationParameters[A, C, F]): AnalyzedPopulation[A, C, F]
}
