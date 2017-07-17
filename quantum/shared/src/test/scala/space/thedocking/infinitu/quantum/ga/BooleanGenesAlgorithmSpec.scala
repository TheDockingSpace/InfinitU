package space.thedocking.infinitu.quantum.ga

import org.junit.runner.RunWith
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.integer.IntegerValue

@RunWith(classOf[JUnitRunner])
class BooleanGenesAlgorithmSpec extends Specification with ShouldMatchers {
  
  //instance a population of 10 individuals with 10 genes each
  //in a quantum runtime this could be 100 simultaneous qubits or iterations over the available qubits
  val population = BooleanGenesPopulation(10, 10)
  
  //instance the standard algorithm to deal with the population
  val algorithm = BooleanGenesAlgorithm()
  
  val fitness = new IndividualFitness[BooleanGenesChromosome, IntegerValue] {
    override def calculate(chromossome: BooleanGenesChromosome) = ???
  }
  
  val pairSelection = ???
  
  val crossover = ???
  
  val mutagen = ???
  
  val surviverSelection = ???
  
//  val parameters = new GenerationParameters[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue, BooleanGenesPopulation]
//  (fitness, pairSelection, crossover, mutagen, surviverSelection)
//  
//  "BooleanGenesAlgorithm" should {
//    "evolve a population" in {
//      //TODO after basic ga is completed
//      val x = algorithm.evolve(population, parameters, 2)
//      success
//    }
//  }
  
}