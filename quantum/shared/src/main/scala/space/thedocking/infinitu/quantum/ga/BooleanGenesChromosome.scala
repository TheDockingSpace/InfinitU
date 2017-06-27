package space.thedocking.infinitu.quantum.ga

import java.lang.{Boolean => JBoolean}

import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.universe.Universe

case class BooleanGenesChromosome(
    override val genes: Universe[Integer1DObjectAddress, JBoolean])
    extends Chromosome[Integer1DObjectAddress, JBoolean]
