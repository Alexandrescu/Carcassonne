import main.scala._
import org.scalatest.FlatSpec
/**
 * Created by Andrei on 28/01/15.
 */

class StateTest extends FlatSpec {
  trait StandardState {
    val state = new State(new StandardTileBag())
    val startTile = SimpleTile("D", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(1), CityEdge(0))
  }

  "A Standard Package State" should "have one tile when initialised" in new StandardState {
    val collection = state.getBoard()
    assert(collection.size == 1)
  }

  it should "have the starting tile when initialised" in new StandardState {
    val tile : Option[Tile] = state.getBoard().get((0,0))
    assert(tile != None)
  }
}
