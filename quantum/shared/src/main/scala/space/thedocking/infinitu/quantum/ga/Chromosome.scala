package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.universe.ObjectAddress

trait ChromosomeOperation[C <: Chromosome[_, _]] {
  def apply(chromosome: C): C
}

//TODO
trait MultiChromosomeOperation[C <: Chromosome[_, _]] {
  def apply(chromosomes: C*): C
}

trait Mutagen[C <: Chromosome[_, _]] extends ChromosomeOperation[C]

trait Chromosome[A <: ObjectAddress, G <: Comparable[_]]
    extends Comparable[Chromosome[A, G]] {

  def genes: Universe[A, G]

}
