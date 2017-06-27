package space.thedocking.infinitu.bool

import space.thedocking.infinitu.dimension.{DimensionValue, DiscreteDimensionValue, Negatable}

//TODO complete other methods, trait implementations and refactor to remove the plus minus stuff
//for now, just a thought, roughly interpreting values as 1 or 0 for plus/minus

trait BooleanValue
    extends DiscreteDimensionValue[Boolean]
    with Negatable[BooleanValue] {
  override val maxValue = TrueValue
  override val minValue = FalseValue
}

object BooleanValue {
  def apply(value: Boolean): BooleanValue =
    if (value) TrueValue else FalseValue
}

case object TrueValue extends BooleanValue {

  override lazy val value: Boolean = true

  override def negate = FalseValue

  override def plus(other: DimensionValue[_]) =
    TrueValue

  override def minus(other: DimensionValue[_]) =
    if (other.asInstanceOf[BooleanValue].value) FalseValue else TrueValue

  override def next = FalseValue

  override def previous = next //but not really :)

}

case object FalseValue extends BooleanValue {

  override lazy val value: Boolean = false

  override def negate = TrueValue

  override def plus(other: DimensionValue[_]) =
    if (other.asInstanceOf[BooleanValue].value) TrueValue else FalseValue

  override def minus(other: DimensionValue[_]) =
    FalseValue

  override def next = TrueValue

  override def previous = next //but not really :)

}
