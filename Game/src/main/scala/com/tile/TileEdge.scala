/**
 * Created by Andrei on 28/01/15.
 *
 * This file defines the TileEdge properties which I will exploit in the Tile class
 * The interesting property is that all the tiles which have a road will need to be
 * handled separately since all the fields are different in terms of property of a
 * player. For this, we assume the edge in a counter-clockwise direction and make
 * a difference between the "before" and "after" piece of grass the road splits.
 */

package com.tile

abstract class TileEdge
case class GrassEdge(grass : GrassSection) extends TileEdge
case class CityEdge(city : CitySection) extends TileEdge
// before, current, after in clockwise fashion
case class RoadEdge(beforeGrass : GrassSection, road : RoadSection, afterGrass: GrassSection) extends TileEdge
