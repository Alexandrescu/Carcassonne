/**
 * Created by Andrei on 28/01/15.
 */

package main.scala

object Direction extends Enumeration {
  type Direction = Value
  val Up, Down, Left, Right = Value
}
import Direction._

abstract class Tile(val identifier : String, up : TileEdge, down : TileEdge, left : TileEdge, right :TileEdge, value : Int) {

  def getTileEdge(direction : Direction) : TileEdge = direction match {
    case Up => up
    case Down => down
    case Right => right
    case Left => left
  }
}

case class SimpleTile(override val identifier : String, up : TileEdge, down : TileEdge, left : TileEdge, right :TileEdge)
  extends Tile(identifier, up, down, left, right, 1)

case class Monastery(override val identifier : String, up : TileEdge, down : TileEdge, left : TileEdge, right :TileEdge)
  extends Tile(identifier, up, down, left, right, 1)

case class BannerTile(override val identifier : String, up : TileEdge, down : TileEdge, left : TileEdge, right :TileEdge)
  extends Tile(identifier, up, down, left, right, 2)