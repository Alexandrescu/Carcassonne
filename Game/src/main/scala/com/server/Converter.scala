package com.server

import com.board.{PossibleMove, Move}
import com.game.Player
import com.server.json.{GameDraw, GameNextMove, GameMove}
import com.tile.Tile

object Converter {
  def moveToJson(move: Move): GameMove = {
    new GameMove()
  }

  def toGameNextMove(tile : Tile, moves : Set[PossibleMove]) : GameNextMove = ???

  def toGameDraw(tile : Tile, player : Player) : GameDraw = ???

  def toMove(move : GameMove) : Move = ???
}
