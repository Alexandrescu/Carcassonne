package com.server

import java.util

import com.board.Direction._
import com.board.{RemovedFollower, Move, PossibleMove}
import com.player.Player
import com.server.json._
import com.tile.{Section, Tile}

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

object Converter {
  def toGameRemoveFollower(move : RemovedFollower): GameRemoveFollower = {
    new GameRemoveFollower(move.place._1, move.place._2, move.sectionId)
  }

  def toGameMove(move: Move): GameMove = {
    val section : Int = move.toOwnFromTile match {
      case Some(s) => s.frontEndId
      case None => -1
    }

    new GameMove(move.tile.identifier, move.place._1, move.place._2, section,
      toGamePlayer(move.player), directionToString(move.tile.orientation))
  }

  def toGamePlayer(player : Player) : GamePlayer = {
    if(player == null) {
      return new GamePlayer(-1, 0, 0)
    }
    new GamePlayer(player.slot, player.followers, player.points)
  }

  def toGameNextMoveList(tile : Tile, moves : Set[PossibleMove]) : GameNextMoveList = {
    val moveList = new ArrayBuffer[GameNextMove]()
    for(move <- moves) {
      addMove(moveList, move)
    }

    new GameNextMoveList(tile.identifier, moveList.toList.toList : util.List[GameNextMove])
  }

  private def addMove(moveList : ArrayBuffer[GameNextMove], possibleMove: PossibleMove): Unit = {
    for(move <- moveList) {
      if((move.x, move.y) == possibleMove.place) {
        move.moves.put(directionToString(possibleMove.direction), toOwnToList(possibleMove.toOwnFromTile))
        return
      }
    }

    val map : util.Map[String, util.List[Integer]] = new util.HashMap[String, util.List[Integer]]()
    map.put(directionToString(possibleMove.direction), toOwnToList(possibleMove.toOwnFromTile))

    moveList += new GameNextMove(map, possibleMove.place._1, possibleMove.place._2)
  }

  def toGameDraw(tile : Tile, player : Player) : GameDraw = {
    new GameDraw(tile.identifier, player.slot)
  }

  def toMove(move : GameMove, tile : Tile, player : Player) : Move = {
    val place = (move.x, move.y)
    val owned: Option[Section] = tile.getSectionById(move.tile.owned)
    tile.orientation = stringToDirection(move.direction)
    new Move(tile, place, owned, player)
  }

  private def stringToDirection(direction : String) : Direction = {
    direction match {
      case "Up" => Up
      case "Down" => Down
      case "Left" => Left
      case "Right" => Right
      case _ => throw new InstantiationError("This is not a valid direction.")
    }
  }

  private def directionToString(direction : Direction): String = {
    direction match {
      case Up => "Up"
      case Down => "Down"
      case Left => "Left"
      case Right => "Right"
    }
  }

  private def toOwnToList(toOwn : List[Option[Section]]) : List[Integer] = {
    def convert(x : Option[Section]) : Integer = x match {
      case Some(s) => s.frontEndId
      case None => -1
    }
    toOwn.map(convert)
  }
}
