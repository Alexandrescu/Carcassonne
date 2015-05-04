package com.board

import Direction.Direction
import com.player.Player
import com.tile.{Section, Tile}

class Move(val tile : Tile, val place : (Int, Int), val toOwnFromTile : Option[Section], val player : Player) {
  override def toString : String = {
    val tileLetter = tile.identifier
    val tileOrientation = tile.orientation
    val owning = if(toOwnFromTile.isDefined) toOwnFromTile.get.frontEndId else "nothing"

    s"Tile $tileLetter at $place facing $tileOrientation by player ${player.slot} owning $owning"
  }
}
class PossibleMove(val direction : Direction, val place : (Int, Int), val toOwnFromTile : List[Option[Section]])

class RemovedFollower(val place : (Int, Int), val sectionId : Int, player: Player) {
  override def toString : String = {
    s"RemovedFollower @ $place, with section $sectionId and player $player."
  }
}
