package com.board

import com.player.Player
import com.tile.Tile

trait Board {
  final type Place = (Int, Int)

  def get(place : Place) : Option[Tile]
  def getBoard() : Map[Place, Tile]

  def setMove(move : Move) : Unit
  def isMove(move : Move) : Boolean

  def getMoves(tile : Tile, player : Player) : Set[PossibleMove]
}
