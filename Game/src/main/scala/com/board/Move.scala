package com.board

import Direction.Direction
import com.player.Player
import com.tile.{Section, Tile}

class Move(val tile : Tile, val place : (Int, Int), val toOwnFromTile : Option[Section], val player : Player) {
  override def toString : String = {
    val tileLetter = tile.identifier
    val tileOrientation = tile.orientation
    val owning = if(toOwnFromTile.isDefined) toOwnFromTile.get.frontEndId else "nothing"
    val playerSlot = if(player == null) "Server" else player.slot

    s"Tile $tileLetter at $place facing $tileOrientation by player $playerSlot owning $owning"
  }
}
class PossibleMove(val direction : Direction, val place : (Int, Int), val toOwnFromTile : List[Option[Section]]) {
  override def toString: String = {
    def sectionToString(section : Option[Section]) : String = section match {
      case Some(s) => s.frontEndId.toString
      case None => (-1).toString
    }

    val sections = (for(section <- toOwnFromTile) yield sectionToString(section)).mkString(", ")
    s"Direction $direction @ $place owning $sections."
  }
}

class RemovedFollower(val place : (Int, Int), val sectionId : Int, player: Player) {
  override def toString : String = {
    s"RemovedFollower @ $place, with section $sectionId and player ${player.slot}."
  }
}
