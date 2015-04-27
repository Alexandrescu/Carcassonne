package com.board

import com.game.Direction
import com.tile._
import Direction._

class StandardLogic extends Logic{

  private def reverse(direction: Direction): Direction = direction match {
    case Up => Down
    case Down => Up
    case Left => Right
    case Right => Left
  }

  def placeTile(thisTile : Tile, tiles : Map[Direction, Tile]): Option[Map[Section, Set[Section]]] = {
    // Unions has all the mappings from the sections to the boardSections
    var unions : Map[Section, Set[Section]] = Map()

    // Exploring each direction for the tile
    tiles.foreach{ case (direction, thatTile) => tileMatch(thisTile, direction, thatTile) match {
      case None => return None
      case Some(sectionMap) => sectionMap.foreach{ case (tileSection, boardSection) => {
        val depSet = unions.getOrElse(tileSection, Set())
        unions = unions - tileSection
        unions = unions + (tileSection -> (depSet + boardSection))
      }}
    }}

    return Some(unions)
  }

  private def tileMatch(tile: Tile, direction : Direction, that : Tile) : Option[Map[Section, Section]] = {
    val thisEdge = tile.getEdge(direction)
    val thatEdge = that.getEdge(reverse(direction))

    (thisEdge, thatEdge) match {
      case (GrassEdge(t), GrassEdge(b)) => {
        Some(Map(t -> b))
      }
      case (RoadEdge(beforeT, t, afterT), RoadEdge(afterB, b, beforeB)) => {
        Some(Map(beforeT -> beforeB, t -> b, afterT -> afterB))
      }
      case (CityEdge(t), CityEdge(b)) => {
        Some(Map(t -> b))
      }
      case _ => None
    }
  }

  override def isMove(tile: Tile, tiles: Map[Direction, Tile]): Option[Map[Section, Set[Section]]] = {
    placeTile(tile, tiles)
  }
}
