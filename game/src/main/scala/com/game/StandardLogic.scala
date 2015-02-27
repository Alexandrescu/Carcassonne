package com.game

import main.scala.Direction._
import main.scala._

import scala.collection.mutable.HashSet
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

class StandardLogic extends Logic{

  private def reverse(direction: Direction): Direction = direction match {
    case Up => Down
    case Down => Up
    case Left => Right
    case Right => Left
  }

  def placeTile(tile : Tile, tiles : Map[Direction, Tile]): Option[HashMap[Int, HashSet[Int]]] = {
    // Unions has all the mappings from the sections to the boardSections
    var unions : HashMap[Int, HashSet[Int]] = new HashMap()

    tiles.foreach{
      case (direction, thatTile) => tileMatch(tile, direction, thatTile) match {
        case None => return None
        case Some(deps) => deps.foreach(p => p match {
          case (TileSection(tileID), BoardSection(boardID)) => {
            unions.get(tileID) match {
              case None => {
                val set = new HashSet() + boardID
                unions += (tileID -> set)
              }
              case Some(set) => {
                unions -= tileID
                unions += (tileID -> (set + boardID))
              }
            }
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

  def tileMatchus(value: Tile, value1: Direction, tile: Tile) : Boolean = {
    var actionList = new ArrayBuffer()



    true
  }

  /** A move is divided into the following stages:
    *
    * 1. Placing new tiles
    *
    * */

  override def isTileMove(thisTile : Tile, tiles : Map[Direction, Tile]): Boolean = {
    tiles.forall {
      case (direction, thatTile) => tileMatchus(thatTile, direction, thisTile)
    }
    None.isEmpty
  }

  // Returns none if not a valid move
  override def validSections(tile: Tile, tiles: Map[Direction, Tile]): Option[List[TileSection]] = ???

  override def isMove(tile: Tile, section: TileSection, tiles: Map[Direction, Tile]): Option[List[Action]] =
    placeTile(tile, tiles) match {
      case None => null
      case Some(dependencies) => {
        var validFollower = true

        dependencies.foreach{case (tileSection, boardSections) => {
          if(section.id == tileSection) {
            validFollower = boardSections.forall()
          }
        }}
      }
    }
}
