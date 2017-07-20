package space.thedocking.infinitu.quantum.ga

import org.junit.runner.RunWith
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import space.thedocking.infinitu.universe.Universe
import space.thedocking.infinitu.integer.Integer1DObjectAddress
import space.thedocking.infinitu.integer.IntegerValue
import space.thedocking.infinitu.quantum.QuantumState._
import scala.math.abs
import scala.util.Random
import java.lang.{Boolean => JBoolean}

@RunWith(classOf[JUnitRunner])
class BooleanGenesAlgorithmSpec extends Specification with ShouldMatchers {
  
  //instance a population of 10 individuals with 10 genes each
  //in a quantum runtime this could be 100 simultaneous qubits or iterations over the available qubits
  val populationSize = 10
  val population = BooleanGenesPopulation(populationSize, 10)
  
  //instance the standard algorithm to deal with the population
  val algorithm = BooleanGenesAlgorithm()
  
  val fitness = new IndividualFitness[BooleanGenesChromosome, IntegerValue] {
    //just as a simple example, let's say the perfect individual would be 0111111111
    val perfect = 511
    //and the fitness is how far the integer representation is from it
    override def calculate(chromossome: BooleanGenesChromosome) = IntegerValue(abs(perfect - chromossome.genes.intValue))
  }
  
  val pairSelection = new ChromosomeSelection[BooleanGenesChromosome] {
    //TODO add method to population to get the population without the randomly selected individual
     override def apply[P <: Population[_, BooleanGenesChromosome, _]](population: P): List[BooleanGenesChromosome] = population.nextRandom :: population.nextRandom :: Nil
  }
  
  val crossover = new MultiChromosomeOperation[BooleanGenesChromosome] {
    
	  //TODO take random index calculation into an Int colapser implementation
    def randomRegion(chromosome: BooleanGenesChromosome) = Random.nextInt(chromosome.size - 2) + 1
    
    override def apply(chromosomes: BooleanGenesChromosome*): BooleanGenesChromosome = {
      require(chromosomes.size > 1, "There should be at least 2 genes to execute a crossover")
      chromosomes.foreach{c => require(c.size > 1, "All chromosomes should have at least 2 genes to execute a crossover")}
      val region = randomRegion(chromosomes.head)
      //TODO add operations to transparently work with Integer1DObjectAddress instead of raw int
      val chromosomeHead = chromosomes.head.genes.toIterable.take(region)
      val chromosomeTail = chromosomes.reverse.head.genes.toIterable.drop(region)
      //XXX what about "twins"? taking other chromosome head/tail combinations would produce more potentially valid and unique individuals
      BooleanGenesChromosome(BooleanGenes(genes = (chromosomeHead ++ chromosomeTail).toMap))
    }
  }
  
  val mutagen = new Mutagen[BooleanGenesChromosome] {
    
    def randomIndex(chromosome: BooleanGenesChromosome) = Random.nextInt(chromosome.size)
    
    override def apply(chromosome: BooleanGenesChromosome): BooleanGenesChromosome = {
      //TODO add mutation factor and mutation probability
      //TODO medium term: mutation by location / proximity to radiation source
      chromosome.copy(genes = chromosome.genes.copy(genes = {
		  val index = randomIndex(chromosome)
				  val originalHead = chromosome.genes.toIterable.take(index)
				  val mutation: Iterable[(Integer1DObjectAddress, JBoolean)] = Iterable((Integer1DObjectAddress(index), Boolean.collapse))
      val chromosomeHead = if (index == 0) {
        mutation ++ originalHead
      } else {
        originalHead ++ mutation
      }
      val chromosomeTail = chromosome.genes.toIterable.drop(index +1)
      (chromosomeHead ++ chromosomeTail).toMap
      }))
    }
  }
  
  val surviverSelection = new ChromosomeGroupSelection[BooleanGenesPopulation] {

    case class ChromosomeByFitness(population: BooleanGenesPopulation) extends Ordering[Integer1DObjectAddress] {
      def compare(a: Integer1DObjectAddress, b: Integer1DObjectAddress): Int =
        population.fitnessValue(a).compare(population.fitnessValue(b))
    }

    override def apply(population: BooleanGenesPopulation): BooleanGenesPopulation = {
      val survivors = population.individuals.keys.toList.sortWith(ChromosomeByFitness(population).lt).take(populationSize)
      population.copy(population.individuals.copy(chromosomes = survivors.map { address =>
        address -> population(address)
      }.toMap),
        survivors.map { address =>
          address -> population.fitnessValue(address)
        }.toMap)
    }
    
  }
  
  val parameters = new GenerationParameters[Integer1DObjectAddress, BooleanGenesChromosome, IntegerValue, BooleanGenesPopulation](fitness, pairSelection, crossover, mutagen, surviverSelection)
  
  "BooleanGenesAlgorithm" should {
    "evolve a population" in {
      //TODO assertions after basic ga is completed
      val x = algorithm.evolve(population, parameters, 2)
      println(x.size)
      println(x.mkString(","))
      success
    }
  }
  
}