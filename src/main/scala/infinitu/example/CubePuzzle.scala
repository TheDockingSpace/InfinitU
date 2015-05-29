package infinitu.example

import infinitu.IntegerTwoDUniverse
import infinitu.IntegerThreeDObjectAddress
import infinitu.IntegerThreeDUniverse
import infinitu.InvalidAddresses
import infinitu.IntegerThreeDObjectAddress

sealed trait Color extends Ordered[Color] {

  def opposite = Color.oppositeTo(this)

  def isOpposite(color: Color) = color == opposite

  def adjacent = Color.adjacentTo(this)

  def isAdjacent(color: Color) = adjacent.contains(color)

  def compare(that: Color): Int = Color.values.indexOf(this) compare Color.values.indexOf(that)

}

case object Color {

  case object First extends Color

  case object Second extends Color

  case object Third extends Color

  case object Fourth extends Color

  case object Fifth extends Color

  case object Sixth extends Color

  val values = List(First, Second, Third, Fourth, Fifth, Sixth)

  // the opposite color calculation could come from a special 1x1x1 cube? or a even simpler list iteration trick?
  val oppositeTo: Map[Color, Color] = Map(First -> Sixth, Second -> Fourth, Third -> Fifth, Fourth -> Second, Fifth -> Third, Sixth -> First)

  val adjacentTo: Map[Color, List[Color]] = values.map { color => color -> values.filterNot { c => c == color || c.opposite == color }.toList }.toMap

}

sealed trait Piece extends Ordered[Piece] {
  def compare(that: Piece): Int = Pieces.values.indexOf(this) compare Pieces.values.indexOf(that)
}

case object Pieces {

  case class CenterPiece(front: Color) extends Piece

  case class CornerPiece(front: Color, top: Color, side: Color) extends Piece

  case class EdgePiece(front: Color, top: Color) extends Piece

  val values: List[Piece] = {
    val pieces = (for (front <- Color.values) yield {
      val center = CenterPiece(front)
      (for (top <- front.adjacent) yield {
        val edge = EdgePiece(front, top)
        val corners: List[Piece] = for (side <- front.adjacent.filterNot(c => c == top || c == front)) yield CornerPiece(front, top, side)
        edge :: corners
      }).flatten
    }).flatten
    pieces
  }

}

case class CubePuzzle(override val objects: Map[IntegerThreeDObjectAddress, Piece] = Map()) extends IntegerThreeDUniverse[Piece](objects = objects)
  with InvalidAddresses[IntegerThreeDObjectAddress] {

  override val name: String = "CubePuzzle"
  override val firstDimensionName = "x"
  override val secondDimensionName = "y"
  override val thirdDimensionName = "z"

  override val invalidAddresses = List(IntegerThreeDObjectAddress(1, 1, 1))

  override def newWithObjects(objects: Map[IntegerThreeDObjectAddress, Piece]) = {
    new CubePuzzle(objects)
  }

}