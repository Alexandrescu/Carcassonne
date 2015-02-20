package com.game

import main.scala.Tile

import scala.collection.immutable.HashMap

class GameBoard extends Board{
  val board : HashMap[Place, Tile] = new HashMap()

  override def get(place : Place): Option[Tile] = {
    if(board.contains(place))
      board.get(place)
    None
  }

  override def setMove(tile: Tile, place : Place, tileSection : Section, follower: PlayerFollower) : Boolean = {
    false
  }

  override def getBoard(): Map[Place, Tile] = {
    board
  }

  override def isMove(tile: Tile, place: Place, tileSection: Section, follower: PlayerFollower): Boolean = {
    true
  }
}
