/**
 * Created by Andrei on 28/01/15.
 *
 * This file defines the TileEdge properties which I will exploit in the Tile class
 * The interesting property is that all the tiles which have a road will need to be
 * handled separately since all the fields are different in terms of property of a
 * player. For this, we assume the edge in a counter-clockwise direction and make
 * a difference between the "before" and "after" piece of grass the road splits.
 */

abstract class TileEdge(sectionId : Integer)
case class RoadEdge(sectionId : Integer) extends TileEdge(sectionId)
case class CityEdge(sectionId : Integer) extends TileEdge(sectionId)
case class GrassEdge(sectionId : Integer, beforeGrassSection : Integer, afterGrassSection : Integer) extends TileEdge(sectionId)
