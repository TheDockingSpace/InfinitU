package space.thedocking.infinitu.quantum.ga

import java.lang.{ Boolean => JBoolean }

import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.bool.BooleanValue
import space.thedocking.infinitu.quantum.QuantumState._
import space.thedocking.infinitu.dimension.Dimension
import space.thedocking.infinitu.integer.IntegerBoolean1DUniverse

case class BooleanGenesChromosome(
  override val genes: BooleanGenes)
    extends Chromosome[Integer1DObjectAddress, JBoolean]

object BooleanGenesChromosome {
  def apply(chromosomeSize: Integer): BooleanGenesChromosome = {
    val genes:  Map[Integer1DObjectAddress, JBoolean] = (for (gene <- 0 until chromosomeSize) 
          yield (Integer1DObjectAddress(gene) -> JBoolean.valueOf(Boolean.collapse)))
          .toMap
    BooleanGenesChromosome(BooleanGenes(genes = genes))
  }
}

case class BooleanGenes(
  override val name: String = "BooleanGenes",
  override val dimensionName: String = "gene",
  val genes: Map[Integer1DObjectAddress, JBoolean] = Map.empty)
    extends IntegerBoolean1DUniverse(bits = genes) {
}
