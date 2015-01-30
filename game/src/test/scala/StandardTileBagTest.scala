import main.scala._
import org.scalatest.FlatSpec

/**
 * Created by Andrei on 30/01/15.
 */
class StandardTileBagTest extends FlatSpec {
  trait StandardPack {
    val tileBag = new StandardTileBag()
    val startTile = SimpleTile("D", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(1), CityEdge(0))
  }

  "StandardTileBag" should "have D as a starting Tile" in new StandardPack {
    assert(tileBag.startTile == startTile)
  }

  it should "have 72 tiles in total" in new StandardPack {
    assert(tileBag.getEntireTileBag.size == 72)
  }

  it should "reduce the number of tiles when getting a new tile" in new StandardPack {
    var count = 0
    while(tileBag.getTile != None)
      count += 1

    assert(count == 71)
  }

  it should "output the only pieces that are in the collections" in new StandardPack {
    var collection = List(tileBag.startTile)
    var tile = tileBag.getTile
    while(tile != None) {
      collection = tile.get :: collection
      tile = tileBag.getTile
    }

    collection = collection.sortBy(x => (x.identifier))
    val entireCollection = tileBag.getEntireTileBag.toList.sortBy(x => (x.identifier))

    // Comparing the two
    val diffSize = entireCollection.zip(collection).filter( tilePair => tilePair._1 != tilePair._2).size
    assert(diffSize == 0)
  }
}
