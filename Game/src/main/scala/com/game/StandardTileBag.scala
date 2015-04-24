/*
 * While writing the pieces, I noticed we can actually deduce the grass property, meaning we could be able to simplify
 * the pieces by not having the Road Edge containing all the data.
 *
 * Yet again, we will need to compute the values at running time, meaning this implementation might be taken into account
 * when implementing the AI for efficiency
 */

package com.game

import com.tile._

class StandardTileBag extends TileBag {
  /*
  val tileList = List(
    (Monastery("A", GrassEdge(0), RoadEdge(0, 0, 0),GrassEdge(0), GrassEdge(0)), 2),
    (Monastery("B", GrassEdge(0), GrassEdge(0), GrassEdge(0), GrassEdge(0)), 4),
    (BannerTile("C", CityEdge(0), CityEdge(0), CityEdge(0), CityEdge(0)), 1),
    (SimpleTile("D", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(1), CityEdge(0)), 3),
    (SimpleTile("E", CityEdge(0), GrassEdge(0), GrassEdge(0), GrassEdge(0)), 5),
    (BannerTile("F", GrassEdge(0), GrassEdge(1), CityEdge(0), CityEdge(0)), 2),
    (SimpleTile("G", CityEdge(0), CityEdge(0), GrassEdge(0), GrassEdge(1)), 1),
    (SimpleTile("H", GrassEdge(0), GrassEdge(0), CityEdge(0), CityEdge(0)), 3),
    (SimpleTile("I", GrassEdge(0), CityEdge(0), GrassEdge(0), CityEdge(1)), 2),
    (SimpleTile("J", CityEdge(0), RoadEdge(0, 0, 1), GrassEdge(0), RoadEdge(1, 0, 0)), 3),
    (SimpleTile("K", RoadEdge(0, 0, 1), GrassEdge(0), RoadEdge(1, 0, 0), CityEdge(0)), 3),
    (SimpleTile("L", RoadEdge(0, 0, 1), RoadEdge(2, 1, 0), RoadEdge(1, 2, 2), CityEdge(0)), 3),
    (BannerTile("M", CityEdge(0), GrassEdge(0), CityEdge(0), GrassEdge(0)), 2),
    (SimpleTile("N", CityEdge(0), GrassEdge(0), CityEdge(0), GrassEdge(0)), 3),
    (BannerTile("O", CityEdge(0), RoadEdge(0, 0, 1), CityEdge(0), RoadEdge(1, 0, 0)), 2),
    (BannerTile("P", CityEdge(0), RoadEdge(0, 0, 1), CityEdge(0), RoadEdge(1, 0, 0)), 3),
    (BannerTile("Q", CityEdge(0), GrassEdge(0), CityEdge(0), CityEdge(0)), 1),
    (SimpleTile("R", CityEdge(0), GrassEdge(0), CityEdge(0), CityEdge(0)), 3),
    (BannerTile("S", CityEdge(0), RoadEdge(0, 0, 1), CityEdge(0), CityEdge(0)), 2),
    (BannerTile("T", CityEdge(0), RoadEdge(0, 0, 1), CityEdge(0), CityEdge(0)), 1),
    (SimpleTile("U", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(0), GrassEdge(0)), 8),
    (SimpleTile("V", GrassEdge(0), RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(0)), 9),
    (SimpleTile("W", GrassEdge(0), RoadEdge(1, 1, 2), RoadEdge(0, 0, 1), RoadEdge(1, 2, 2)), 4),
    (SimpleTile("X", RoadEdge(0, 0, 1), RoadEdge(2, 2, 3), RoadEdge(1, 1, 2), RoadEdge(2, 3, 0)),1)
  )

  val startTile : Tile = SimpleTile("D", RoadEdge(0, 0, 1), RoadEdge(1, 0, 0), GrassEdge(1), CityEdge(0))
  private var bag : List[Tile] = List.empty

  for((tile, times) <- tileList) {
    bag = List.fill(times)(tile) ++ bag
  }

  bag = Random.shuffle(bag)
  private val entireBag = startTile :: bag

  override def getEntireTileBag: Iterable[Tile] = entireBag

  override def getTile: Option[Tile] = {
    bag match {
      case head :: tail => {
        bag = tail
        Some(head)
      }
      case _ => None
    }
  }
  */

  val dSection1 = new GrassSection(1)
  val dSection2 = new RoadSection(2)
  val dSection3 = new GrassSection(3)
  val dSection4 = new CitySection(4)

  val dTile = new SimpleTile("D",
    new RoadEdge(dSection1, dSection2, dSection3),
    new RoadEdge(dSection3, dSection2, dSection1),
    new GrassEdge(dSection1),
    new CityEdge(dSection4)
  )

  val dTile2 = new SimpleTile("D",
    new RoadEdge(dSection1, dSection2, dSection3),
    new RoadEdge(dSection3, dSection2, dSection1),
    new GrassEdge(dSection1),
    new CityEdge(dSection4)
  )

  override val startTile: Tile = dTile

  override def getEntireTileBag: Iterable[Tile] = ???

  override def finished: Boolean = false

  override def getTile: Option[Tile] = Some(dTile2)
}