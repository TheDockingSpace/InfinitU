package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.universe.ObjectAddress

trait Population[A <: ObjectAddress, C <: Chromosome[A, _]] {

  def individuals: Universe[A, C]
  def newGeneration: Population[A, C]
  def evolve(times: Int): Seq[Population[A, C]]

}
