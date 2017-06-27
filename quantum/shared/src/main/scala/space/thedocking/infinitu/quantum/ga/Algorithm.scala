package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.dimension.DiscreteDimensionValue
import space.thedocking.infinitu.universe.ObjectAddress

trait Algorithm[A <: ObjectAddress, C <: Chromosome[A, _], F <: DiscreteDimensionValue[_]] {
  def evolve(population: Population[A, C, F],
    parameters: GenerationParameters[A, C, F],
    times: Int): Stream[Population[A, C, F]]
  def evolve(
    population: Population[A, C, F],
    parameters: GenerationParameters[A, C, F]): Population[A, C, F]
}