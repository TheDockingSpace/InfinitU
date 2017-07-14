package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.dimension.DiscreteDimensionValue
import space.thedocking.infinitu.universe.ObjectAddress

trait Algorithm[A <: ObjectAddress, C <: Chromosome[A, _], F <: DiscreteDimensionValue[_], P <: Population[A, C, F]] {
  def evolve(population: P,
    parameters: GenerationParameters[A, C, F, P],
    times: Int): Stream[P]
  def evolve(
    population: P,
    parameters: GenerationParameters[A, C, F, P]): P
}