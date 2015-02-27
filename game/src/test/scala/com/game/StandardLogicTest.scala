package com.game

import main.scala._
import main.scala.Direction._
import org.scalatest.FlatSpec

class StandardLogicTest extends FlatSpec {
  val logic = new StandardLogic()

  trait StartTile {
    val startTile = SimpleTile("D",
      RoadEdge(TileSection(1), TileSection(2), TileSection(3)),
      RoadEdge(TileSection(3), TileSection(2), TileSection(1)),
      GrassEdge(TileSection(1)), CityEdge(TileSection(4)))
  }

  trait AdjacentTiles {
    // Tile U
    val up = SimpleTile("Up",
      RoadEdge(BoardSection(1), BoardSection(2), BoardSection(3)),
      RoadEdge(BoardSection(3), BoardSection(2), BoardSection(1)),
      GrassEdge(BoardSection(1)), GrassEdge(BoardSection(3))
    )
    val down = SimpleTile("Down",
    RoadEdge(BoardSection(7), BoardSection(11), BoardSection(8)),
    CityEdge(BoardSection(10)),
    RoadEdge(BoardSection(9), BoardSection(12), BoardSection(7)),
    RoadEdge(BoardSection(8), BoardSection(13), BoardSection(9)))

    val grass4 = GrassEdge(BoardSection(4))
    val left = Monastery("Left", grass4, grass4, grass4, grass4)

    val grass6 = GrassEdge(BoardSection(6))
    val right = BannerTile("Right", grass6, grass6, CityEdge(BoardSection(5)), grass6)

    val goodNeighbours = Map(Up -> up, Down -> down, Left -> left, Right -> right)
    val badNeighbours = Map(Up -> down,  Down -> down, Left -> left, Right -> right)
  }


  "Standard logic" should "accept if we place a 'start tile' (no other neighbour)" in new StartTile{
    //assert(logic.isTileMove(startTile, Map()))
  }

  it should "accept tile placement if there are matching neighbours" in new StartTile with AdjacentTiles{
    assert(logic.isMove(startTile, goodNeighbours).isDefined == true)
  }
  it should "not accept tile placement if at least one neighbour is not matching" in new StartTile with AdjacentTiles {
    assert(logic.isMove(startTile, badNeighbours).isEmpty == true)
  }
  it should "return valid dependencies when move is valid" in new StartTile with AdjacentTiles {
    val dependencies = logic.isMove(startTile, goodNeighbours)

    // Check if valid move
    assert(dependencies.isDefined == true)

    val map = dependencies.get
    assert(map(TileSection(1)).toList.map(section => section.id).sortWith(_ < _) == List(1, 4, 7))
    assert(map(TileSection(2)).toList.map(section => section.id).sortWith(_ < _) == List(2, 11))
    assert(map(TileSection(3)).toList.map(section => section.id).sortWith(_ < _) == List(3, 8))
    assert(map(TileSection(4)).toList.map(section => section.id).sortWith(_ < _) == List(5))
  }
}
