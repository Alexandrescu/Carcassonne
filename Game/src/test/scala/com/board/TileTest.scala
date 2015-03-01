package com.board

import com.tile.{RoadEdge, CityEdge, GrassEdge, SimpleTile}
import main.scala.Direction._
import main.scala.RoadEdge
import org.scalatest.FlatSpec

class TileTest extends FlatSpec {
  trait StartTile {
    val identifier = "D"
    val tile = SimpleTile(identifier,
      RoadEdge(TileSection(1), TileSection(2), TileSection(3)),
      RoadEdge(TileSection(3), TileSection(2), TileSection(1)),
      GrassEdge(TileSection(1)), CityEdge(TileSection(4)))

    val upEdge = RoadEdge(TileSection(1), TileSection(2), TileSection(3))
    val downEdge = RoadEdge(TileSection(3), TileSection(2), TileSection(1))
    val leftEdge = GrassEdge(TileSection(1))
    val rightEdge = CityEdge(TileSection(4))
  }
  "Tile" should "have a identifier" in new StartTile {
    assert(tile.identifier == identifier)
  }

  it should "have default orientation Up" in new StartTile {
    assert(tile.orientation == Up)
  }

  it should "change ®the tileEdges when changing orientation to Left" in new StartTile {
    assert(tile.orientation == Up &&
      tile.up == upEdge && tile.down == downEdge &&
      tile.left == leftEdge && tile.right == rightEdge)

    tile.orientation = Left

    assert(tile.orientation == Left &&
      tile.up == leftEdge && tile.down == rightEdge &&
      tile.left == downEdge && tile.right == upEdge)
  }

  // This doesn't seem like a great idea
  it should "assign orientation only once" in new StartTile {
    val newOrientation = Down
    val otherOrientation = Left

    assert(tile.orientation == Up)

    tile.orientation = newOrientation
    assert(tile.orientation == newOrientation)

    tile.orientation = otherOrientation
    assert(tile.orientation == newOrientation)
  }
}