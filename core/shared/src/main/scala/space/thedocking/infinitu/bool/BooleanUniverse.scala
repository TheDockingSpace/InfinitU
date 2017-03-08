package space.thedocking.infinitu.bool

import space.thedocking.infinitu.dimension.DiscreteDimensionValue
import space.thedocking.infinitu.dimension.DimensionValue
import space.thedocking.infinitu.dimension.Finite
import java.lang.{Boolean => JBoolean}

//TODO complete other methods, trait implementations and refactor to remove the plus minus stuff
//for now, just a thought, roughly interpreting values as 1 or 0 for plus/minus

trait BooleanValue
//scala's boolean is not comparable???
    extends DiscreteDimensionValue[JBoolean]

case object TrueValue extends BooleanValue {

  override val value: JBoolean = true

  override def plus(other: DimensionValue[JBoolean]) =
    TrueValue

  override def minus(other: DimensionValue[JBoolean]) =
    if (other.value) FalseValue else TrueValue

  override def next = FalseValue

  override def previous = next //but not really :)

}

case object FalseValue extends BooleanValue {

  override val value: JBoolean = false

  override def plus(other: DimensionValue[JBoolean]) =
    if (other.value) TrueValue else FalseValue

  override def minus(other: DimensionValue[JBoolean]) =
    FalseValue

  override def next = TrueValue

  override def previous = next //but not really :)

}
