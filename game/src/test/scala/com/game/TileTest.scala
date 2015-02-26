package com.game

import main.scala.Direction._
import main.scala.{CityEdge, GrassEdge, RoadEdge, SimpleTile}
import org.scalatest.FlatSpec

class TileTest extends FlatSpec {
  trait StartTile {
    val identifier = "D"
    val tile = SimpleTile(identifier,
      RoadEdge(Section(1), Section(2), Section(3)),
      RoadEdge(Section(3), Section(2), Section(1)),
      GrassEdge(Section(1)), CityEdge(Section(4)))

    val upEdge = RoadEdge(Section(1), Section(2), Section(3))
    val downEdge = RoadEdge(Section(3), Section(2), Section(1))
    val leftEdge = GrassEdge(Section(1))
    val rightEdge = CityEdge(Section(4))
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
