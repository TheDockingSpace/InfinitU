package space.thedocking.infinitu.quantum.ga

import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.dimension.DiscreteDimensionValue

class BooleanGenesPopulation(
    override val individuals: Universe[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    extends Population[Integer1DObjectAddress, BooleanGenesChromosome] {

  override def evolve(times: Int) = {
    for (generation <- 1 to times) {}
    ???
  }

}

class BooleanGenesAnalyzedPopulation(
    override val individuals: Universe[Integer1DObjectAddress,
                                       BooleanGenesChromosome])
    extends BooleanGenesPopulation(individuals)
    with AnalyzedPopulation[Integer1DObjectAddress, BooleanGenesChromosome] {

  override def newGeneration[V <: DiscreteDimensionValue[_]](
      fitness: Either[IndividualFitness[BooleanGenesChromosome, V],
                      RelativeFitness[BooleanGenesChromosome,
                                      Population[Integer1DObjectAddress,
                                                 BooleanGenesChromosome],
                                      V]],
      //TODO support multi-individual crossover
      pairSelection: ChromosomeSelection[BooleanGenesChromosome],
      crossover: ChromosomeOperation[BooleanGenesChromosome],
      mutagen: Mutagen[BooleanGenesChromosome],
      surviverSelection: ChromosomeGroupSelection[
        _ <: Universe[_ <: Integer1DObjectAddress,
                      _ <: BooleanGenesChromosome]])
    : AnalyzedPopulation[Integer1DObjectAddress, BooleanGenesChromosome]
}
