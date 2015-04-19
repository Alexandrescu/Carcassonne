package com.server

import com.board.{PossibleMove, Move}
import com.game.Player
import com.server.json.{GamePlayer, GameDraw, GameNextMove, GameMove}
import com.tile.Tile

object Converter {
  def moveToJson(move: Move): GameMove = {
    val section : Int = move.toOwnFromTile match {
      case Some(s) => s.frontEndId
      case None => -1
    }

    new GameMove(move.tile.identifier, move.place._1, move.place._2, section, toGamePlayer(move.player))
  }

  def toGamePlayer(player : Player) : GamePlayer = ???

  def toGameNextMove(tile : Tile, moves : Set[PossibleMove]) : GameNextMove = ???

  def toGameDraw(tile : Tile, player : Player) : GameDraw = ???

  def toMove(move : GameMove) : Move = ???
}
