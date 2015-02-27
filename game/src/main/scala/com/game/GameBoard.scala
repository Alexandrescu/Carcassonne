package com.game

import main.scala.Direction._
import main.scala.Direction.Direction
import main.scala.{Player, Direction, Tile}

import scala.collection.mutable._


class GameBoard(logic: Logic, unionKeeper : UnionKeeper) extends Board{
  val BaseSection = unionKeeper.base
  val board : scala.collection.immutable.HashMap[Place, Tile] = new scala.collection.immutable.HashMap()

  private def canOwn(boardSections: Option[HashSet[BoardSection]]) : Boolean =  boardSections match {
    case None => true
    case Some(sections) => sections.forall(unionKeeper.isOwned)
  }

  override def get(place : Place): Option[Tile] = board.get(place)

  def updateBoard(tile: Tile, place : Place, deps: HashMap[TileSection, HashSet[BoardSection]], player: Player, toOwn : TileSection): Unit = {
    var freeSections = tile.getSections()

    // Union-ing the dependencies
    deps.foreach{case (tileSection, boardSections) => {
      if(freeSections.contains(tileSection) == false) {
        throw new Error("This is not something we have on the tile")
      }
      freeSections = freeSections - tileSection

      val newSection = boardSections.foldLeft(BaseSection){case (sectA, sectB) => unionKeeper.union(sectA, sectB)}
      if(tileSection == toOwn) {
        unionKeeper.own(newSection, player)
      }
      tile.updateSection(tileSection, newSection)
    }}

    freeSections.foreach(section => {
      val newSection = unionKeeper.add(section)
      tile.updateSection(section, newSection)
    })
  }

  override def setMove(tile: Tile, place : Place, toOwn : TileSection, player : Player) : Boolean = {
    val move = isMove(tile, place)
    if(move.isEmpty) return false
    val dependencies = move.get

    if(!canOwn(dependencies.get(toOwn))) return false

    updateBoard(tile, place, dependencies, player, toOwn)
    true
  }

  def isMove(tile : Tile, place : Place): Option[HashMap[TileSection, HashSet[BoardSection]]] = {
    var map : scala.collection.immutable.Map[Direction, Tile] = scala.collection.immutable.HashMap()
    Direction.values.foreach(direction => getTile(place, direction) match {
      case Some(tile) => map = map + (direction -> tile)
      case _ => {}
    })
    logic.isMove(tile, map)
  }

  // This implies that a Tile must include sufficient information to be able to do this
  // The board could connect with the Logic so that if the logic needs more details it can query the board
  override def isMove(tile: Tile, place: Place, whereToPlace: TileSection, follower: Follower): Boolean = {
    true
  }
  override def getBoard(): scala.collection.immutable.Map[Place, Tile] = {
    board
  }

  implicit def directionToPlace(dir : Direction) : Place = dir match {
    case Up => (0, 1)
    case Down => (0, -1)
    case Left => (-1, 0)
    case Right => (1, 0)
  }
  private def add(place1 : Place, place2 : Place): Place = {
    (place1._1 + place2._1, place1._2 + place2._2)
  }
  private def getTile(place : Place, direction : Direction) : Option[Tile] = {
    get(add(place, direction))
  }


}
