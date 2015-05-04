import main.scala._
import org.scalatest.{Matchers, FlatSpec}
/**
 * Created by Andrei on 28/01/15.
 */

trait StandardStateBehaviour { this: FlatSpec =>

  val state = new State(new StandardTileBag())
  val startTile = SimpleTile("D", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(1), CityEdge(0))

  def firstPossibleMoves(moves : Integer, toBePlacedTile : Tile): Unit = {

    it should "have the Start Tile when instantiated" in {
      val firstTile: Option[Tile] = state.getBoard().get(0, 0)
      assert(firstTile.get == startTile)
    }

    ignore should s"have $moves for adding $toBePlacedTile" in {
      val moveCollection = state.getPossibleMoves(toBePlacedTile)
      assert(moves == moveCollection.size)
    }
  }
  
  def afterFirstMove(moves : Integer, placedTile : Tile, position : (Integer, Integer)) : Unit = {
    it should s"allow to place tile $placedTile at $position" in {
      assert(state.playMove(placedTile, position))
    }

    it should "have 2 tiles after the tile has been placed" in {
      assert(state.getBoard().size == 2)
    }

    it should s"have 6 edges after placing $placedTile" in {
      assert(state.getBorderEdges().size == 6)
    }
  }
}

class StateTest extends FlatSpec with Matchers with StandardStateBehaviour {
  "A Standard Package State" should "have one tile when initialised" in {
    val collection = state.getBoard()
    assert(collection.size == 1)
  }

  val gcrTile : Tile = SimpleTile("GCRTile", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(1), CityEdge(0))
  var rTile : Tile = SimpleTile("RTile", RoadEdge(0, 0, 1), RoadEdge(2, 2, 3), RoadEdge(1, 1, 2), RoadEdge(2, 3, 0))

  "Adding Tile D to a Standard Pack" should behave like firstPossibleMoves(4, gcrTile)

  "Adding Tile X to a Standard Pack" should behave like firstPossibleMoves(2, rTile)
}
