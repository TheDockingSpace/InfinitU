package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.universe.ObjectAddress

trait Chromosome[A <: ObjectAddress, G <: Comparable[_]]
    extends Comparable[Chromosome[A, G]] {

  def genes: Universe[A, G]
  def mutate: Chromosome[A, G]
  def crossover(other: Chromosome[A, G])

}
