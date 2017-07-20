package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.universe.{ObjectAddress, Universe}

trait ChromosomeOperation[C <: Chromosome[_, _]] {
  def apply(chromosome: C): C
}

//TODO
trait MultiChromosomeOperation[C <: Chromosome[_, _]] {
  def apply(chromosomes: C*): C
}

trait Mutagen[C <: Chromosome[_, _]] extends ChromosomeOperation[C]

trait Chromosome[A <: ObjectAddress, G <: Comparable[_]]
    extends Comparable[Chromosome[A, G]]
    with Universe[A, G] {

  val genes: Universe[A, G]
  
  override val objects = genes.objects

  override def compareTo(other: Chromosome[A, G]) = {
    if (this.genes.size != other.genes.size) {
      //assumes that all addresses exists in both chromosomes and are dense up to some initialization point
      this.genes.size - other.genes.size
    } else {
      this.genes.keys.find(address =>
        !(this.genes.get(address).equals(other.genes.get(address))))
        .map(address => this.genes.get(address).get.compareTo(other.genes.get(address).get.asInstanceOf))
        .getOrElse(0)
    }
  }

}
