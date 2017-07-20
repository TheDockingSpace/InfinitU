package space.thedocking.infinitu.quantum.ga

trait EvolutionListener[P <: Population[_, _, _]] {
  def onNewGeneration(population: P): Unit = {}
}

case object DoNothingEvolutionListener extends EvolutionListener[Population[_, _, _]]

object EvolutionListener {
  def doNothing[P <: Population[_, _, _]]: EvolutionListener[P] = DoNothingEvolutionListener.asInstanceOf[EvolutionListener[P]]
}

case object PrintlnEvolutionListener extends EvolutionListener[Population[_, _, _]] {
  override def onNewGeneration(population: Population[_, _, _]): Unit = println(s"onNewGeneration population:$population")
}

case class MultiEvolutionListener(listeners: EvolutionListener[Population[_, _, _]]*) extends EvolutionListener[Population[_, _, _]] {
  override def onNewGeneration(population: Population[_, _, _]): Unit = listeners.foreach(_.onNewGeneration(population))
}