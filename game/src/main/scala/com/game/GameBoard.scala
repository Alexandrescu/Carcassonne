package com.game

import main.scala.Direction._
import main.scala.Direction.Direction
import main.scala.{Player, Direction, Tile}

import scala.collection.mutable._


class GameBoard(logic: Logic, unionKeeper : UnionKeeper) extends Board{
  val BaseSection = unionKeeper.base
  val board : scala.collection.immutable.HashMap[Place, Tile] = new scala.collection.immutable.HashMap()
  val boardOutline : scala.collection.immutable.HashMap[Place, Tile] = new scala.collection.immutable.HashMap()

  private def canOwn(boardSections: Option[HashSet[BoardSection]]) : Boolean =  boardSections match {
    case None => true
    case Some(sections) => sections.forall(unionKeeper.isOwned)
  }

  // No going back now
  private def updateBoard(tile: Tile, place : Place, deps: HashMap[TileSection, HashSet[BoardSection]], toOwnPair : Option[(TileSection, Player)]): Unit = {
    var freeSections = tile.getSections()

    // Union-ing the dependencies
    deps.foreach{case (tileSection, boardSections) =>
      if(!freeSections.contains(tileSection)) {
        throw new Error("Union-ing something which is not on the tile")
      }
      freeSections = freeSections - tileSection

      val newSection = boardSections.foldLeft(BaseSection){case (sectA, sectB) => unionKeeper.union(sectA, sectB)}
      if(toOwnPair.isDefined && tileSection == toOwnPair.get._1) {
        unionKeeper.own(newSection, toOwnPair.get._2)
      }
      tile.updateSection(tileSection, newSection)
    }

    freeSections.foreach(section => {
      val newSection = unionKeeper.add(section)
      tile.updateSection(section, newSection)
    })
  }

  override def get(place : Place): Option[Tile] = board.get(place)

  override def getBoard(): scala.collection.immutable.Map[Place, Tile] = board

  override def setMove(tile: Tile, place : Place, toOwnPair : Option[(TileSection, Player)]) : Unit = {
    val toOwn : Option[TileSection] = if(toOwnPair.isEmpty) None else Some(toOwnPair.get._1)
    val move = isMove(tile, place, toOwn)

    val dependencies = move.get

    updateBoard(tile, place, dependencies, toOwnPair)
  }

  override def isMove(tile : Tile, place : Place, toOwn : Option[TileSection]): Option[HashMap[TileSection, HashSet[BoardSection]]] = {
    // Generating places to test for move in Logic
    var map : scala.collection.immutable.Map[Direction, Tile] = scala.collection.immutable.HashMap()
    Direction.values.foreach(direction => getTile(place, direction) match {
      case Some(tile) => map = map + (direction -> tile)
      case _ => {}
    })
    // Getting back the union dependencies created by placing the tiles
    val dependencies = logic.isMove(tile, map)

    // Checking if player can own the dependency he wants
    if(dependencies.isEmpty) return None
    val actualDependencies = dependencies.get

    toOwn match {
      case None => dependencies
      case Some(toOwnSection) => {
        if(canOwn(actualDependencies.get(toOwnSection))) {
          return dependencies
        }
        else {
          return None
        }
      }
    }
  }

  // This implies that a Tile must include sufficient information to be able to do this
  // The board could connect with the Logic so that if the logic needs more details it can query the board

  implicit def directionToPlace(dir : Direction) : Place = dir match {
    case Up => (0, 1)
    case Down => (0, -1)
    case Left => (-1, 0)
    case Right => (1, 0)
  }
  protected def add(place1 : Place, place2 : Place): Place = {
    (place1._1 + place2._1, place1._2 + place2._2)
  }
  protected def getTile(place : Place, direction : Direction) : Option[Tile] = {
    get(add(place, direction))
  }
}
