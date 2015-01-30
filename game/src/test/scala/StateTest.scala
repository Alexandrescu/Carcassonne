import main.scala._
import org.scalatest.{Matchers, FlatSpec}
/**
 * Created by Andrei on 28/01/15.
 */

trait StandardStateBehaviour { this: FlatSpec =>

  val state = new State(new StandardTileBag())
  val startTile = SimpleTile("D", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(1), CityEdge(0))

  def firstPossibleMoves(moves : Integer, placedTile : Tile): Unit = {

    it should "have the Start Tile when instantiated" in {
      val firstTile: Option[Tile] = state.getBoard().get(0, 0)
      assert(firstTile.get == startTile)
    }

    ignore should s"have $moves for adding $placedTile" in {
      val moveCollection = state.getPossibleMoves(placedTile)
      assert(moves == moveCollection.size)
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
