package com.game

import main.scala.Direction._
import main.scala._

import scala.collection.mutable.{HashMap, HashSet}

class StandardLogic extends Logic{

  private def reverse(direction: Direction): Direction = direction match {
    case Up => Down
    case Down => Up
    case Left => Right
    case Right => Left
  }

  def placeTile(tile : Tile, tiles : Map[Direction, Tile]): Option[HashMap[TileSection, HashSet[BoardSection]]] = {
    // Unions has all the mappings from the sections to the boardSections
    var unions : HashMap[TileSection, HashSet[BoardSection]] = new HashMap()

    tiles.foreach{
      case (direction, thatTile) => tileMatch(tile, direction, thatTile) match {
        case None => return None
        case Some(deps) => deps.foreach(p => p match {
          case (TileSection(tileID), BoardSection(boardID)) => {
            unions.get(TileSection(tileID)) match {
              case None => {
                val set = new HashSet() + BoardSection(boardID)
                unions += (TileSection(tileID) -> set)
              }
              case Some(set) => {
                unions -= TileSection(tileID)
                unions += (TileSection(tileID) -> (set + BoardSection(boardID)))
              }
            }
          }
          case _ => {
            throw new Error("We should have got a (TileSection, BoardSection) pair.")
          }
        })
      }
    }

    return Some(unions)
  }

  def tileMatch(tile: Tile, direction : Direction, that : Tile) : Option[Map[Section, Section]] = {
    val thisEdge = tile.getEdge(direction)
    val thatEdge = that.getEdge(reverse(direction))

    (thisEdge, thatEdge) match {
      case (GrassEdge(t1), GrassEdge(b1)) => {
        Some(Map(t1 -> b1))
      }
      case (RoadEdge(beforeT1, t1, afterT1), RoadEdge(afterB1, b1, beforeB1)) => {
        Some(Map(beforeT1 -> beforeB1, t1 -> b1, afterT1 -> afterB1))
      }
      case (CityEdge(t1), CityEdge(b1)) => {
        Some(Map(t1 -> b1))
      }
      case _ => None
    }
  }

  override def isMove(tile: Tile, tiles: Map[Direction, Tile]): Option[HashMap[TileSection, HashSet[BoardSection]]] = {
    placeTile(tile, tiles)
  }
}
