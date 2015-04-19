package com.server

import com.board.{PossibleMove, Move}
import com.game.Direction._
import com.game.Player
import com.server.json._
import com.tile.{Section, Tile}

import scala.collection.JavaConversions._

object Converter {
  def moveToJson(move: Move): GameMove = {
    val section : Int = move.toOwnFromTile match {
      case Some(s) => s.frontEndId
      case None => -1
    }

    new GameMove(move.tile.identifier, move.place._1, move.place._2, section, toGamePlayer(move.player))
  }

  def toGamePlayer(player : Player) : GamePlayer = {
    new GamePlayer(player.slot, player.followers, player.points)
  }

  def toGameNextMove(tile :Tile, possibleMove: PossibleMove): GameNextMove = {
    val gameTileMove : GameTileMove =
      new GameTileMove(toStringDirection(possibleMove.direction), toOwnToList(possibleMove.toOwnFromTile))
    new GameNextMove(tile.identifier, gameTileMove, possibleMove.place._1, possibleMove.place._2)
  }

  def toGameNextMoveList(tile : Tile, moves : Set[PossibleMove]) : GameNextMoveList = {
    new GameNextMoveList(
      (for(move <- moves) yield toGameNextMove(tile, move)).toList
    )
  }

  def toGameDraw(tile : Tile, player : Player) : GameDraw = ???

  def toMove(move : GameMove) : Move = ???

  def toStringDirection(direction : Direction): String = {
    direction match {
      case Up => "Up"
      case Down => "Down"
      case Left => "Left"
      case Right => "Right"
    }
  }

  def toOwnToList(toOwn : List[Option[Section]]) : List[Integer] = {
    def convert(x : Option[Section]) : Integer = x match {
      case Some(s) => s.frontEndId
      case None => -1
    }
    toOwn.map(convert)
  }
}
