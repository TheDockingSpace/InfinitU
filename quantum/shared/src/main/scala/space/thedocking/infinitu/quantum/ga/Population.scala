package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.dimension.DiscreteDimensionValue
import space.thedocking.infinitu.universe.{ObjectAddress, Universe}

trait Fitness[C <: Chromosome[_, _], +F <: DiscreteDimensionValue[_]]

trait IndividualFitness[C <: Chromosome[_, _], +F <: DiscreteDimensionValue[_]]
    extends Fitness[C, F] {
  def calculate(individual: C): F
}

trait RelativeFitness[C <: Chromosome[_, _],
                      F <: DiscreteDimensionValue[_],
                      P <: Population[_, C, F]]
    extends Fitness[C, F] {
  def individualFitness: IndividualFitness[C, F]
  def calculate(individuals: C, population: P): F
}

trait ChromosomeSelection[C <: Chromosome[_, _]] {
  def apply[U <: Universe[_, C]](individuals: U): C
}

trait ChromosomeGroupSelection[U <: Universe[_, _]] {
  def apply(individuals: U): U
}

case class GenerationParameters[A <: ObjectAddress, C <: Chromosome[A, _], F <: DiscreteDimensionValue[_], P<: Population[A, C, F]](
    val fitness: Either[
      IndividualFitness[C, _ <: F],
      RelativeFitness[C, F, P]],
    //TODO support multi-individual crossover
    val pairSelection: ChromosomeSelection[C],
    val crossover: ChromosomeOperation[C],
    val mutagen: Mutagen[C],
    val surviverSelection: ChromosomeGroupSelection[
      _ <: Universe[_ <: A, _ <: C]]
)

trait Population[A <: ObjectAddress, C <: Chromosome[A, _], F <: DiscreteDimensionValue[_]] {
  def individuals: Universe[A, C]
	//TODO instead of this map like behaviour, replace by some analysis result class where statistics can be attached
  def fitnessValue(individualAddress: A): F

	//TODO think about renaming address to key...
  def keys: Iterable[A] = individuals.keys

  def values: Iterable[C] = individuals.values

  def toIterable: Iterable[(A, C)] = individuals.toIterable
  
  def size: Int = individuals.size

  def map[B](f: ((A, C)) => B) = individuals.map(f);

}
