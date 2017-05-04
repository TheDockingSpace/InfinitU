package space.thedocking.infinitu.list

import space.thedocking.infinitu.dimension._
import space.thedocking.infinitu.universe._

trait ListValue[V <: Comparable[_]] extends DiscreteDimensionValue[List[V]] {

  override val value: List[V] = Nil

}

class DimensionValueListValue[V <: DimensionValue[_]](
    override val value: List[V] = Nil
) extends DimensionValue[List[V]] {

  //TODO general refactoring moving operators to trait...
  override def plus(other: DimensionValue[_]) = {
    new DimensionValueListValue(
      value.zip(other.value.asInstanceOf[List[V]]).map {
        case (x, y) => x.plus(y.asInstanceOf[V]).asInstanceOf[V]
      })
  }

  override def minus(other: DimensionValue[_]) =
    new DimensionValueListValue[V](
      value.zip(other.value.asInstanceOf[List[V]]).map {
        case (x, y) => x.minus(y.asInstanceOf[V]).asInstanceOf[V]
      })

  override lazy val minValue: DimensionValue[List[V]] =
    new DimensionValueListValue(value.map { x =>
      x.minValue
    }).asInstanceOf

  override lazy val maxValue: DimensionValue[List[V]] =
    new DimensionValueListValue(value.map { x =>
      x.maxValue
    }).asInstanceOf

  //TODO refactor to support heterogeneous lists
  def patchWhere(p: (V) => Boolean,
                 that: (Int) => V,
                 overflow: => List[V]): List[V] = {
    val i = value.lastIndexWhere(p)
    if (i == -1) {
      overflow
    } else {
      val incremented = value.patch(i, Seq(that(i)), 1)
      if (i == value.length - 1) {
        incremented
      } else {
        incremented.patch(i + 1,
                          Seq(overflow(i + 1).asInstanceOf[V]),
                          value.size - (i + 1))
      }
    }
  }

}

case class DiscreteDimensionValueListValue[V <: DiscreteDimensionValue[_]](
    override val value: List[V] = Nil)
    extends DimensionValueListValue[V]
    with DiscreteDimensionValue[List[V]] {

  //TODO replace this rewrapping code
  override def plus(other: DimensionValue[_]) =
    new DiscreteDimensionValueListValue(super.plus(other).value)

  override def minus(other: DimensionValue[_]) =
    new DiscreteDimensionValueListValue(super.minus(other).value)

  override lazy val minValue: DimensionValue[List[V]] =
    DiscreteDimensionValueListValue[V](value.map { x =>
      x.minValue.asInstanceOf[V]
    })

  override lazy val maxValue: DimensionValue[List[V]] =
    DiscreteDimensionValueListValue[V](value.map { x =>
      x.maxValue.asInstanceOf[V]
    })

  override def next =
    DiscreteDimensionValueListValue(
      patchWhere(x => !x.isMaxValue,
                 i => value(i).next.asInstanceOf[V],
                 minValue.value))

  override def previous =
    DiscreteDimensionValueListValue(
      patchWhere(x => !x.isMinValue,
                 i => value(i).previous.asInstanceOf[V],
                 maxValue.value))

}
