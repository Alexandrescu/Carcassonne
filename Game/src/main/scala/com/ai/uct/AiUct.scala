package com.ai.uct

import com.ai.AI
import com.board.{Move, PossibleMove, RemovedFollower}
import com.client.Client
import com.server.Converter
import com.server.json.GameMove
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

class AiUct(client : Client, gameSize : Int) extends AI{
  var moveList: ArrayBuffer[Either[Move, RemovedFollower]] = null

  override def getMove(tile: Tile, moves: Set[PossibleMove]): GameMove = {
    val search = new UCT(moveList, gameSize, Some(tile), client.slot)
    //val move = search.uctSearch(30)
    val move = search.uctSearchSeconds(15)

    val sectionId : Int = move.toOwnFromTile match {
      case Some(section) => section.frontEndId
      case None => -1
    }

    new GameMove(move.place._1, move.place._2, Converter.directionToString(move.tile.orientation), sectionId)
  }

  override def movePlayed(move: Either[Move, RemovedFollower]): Unit = {
    moveList += move
  }

  override def currentState(moveOrFollowers: ArrayBuffer[Either[Move, RemovedFollower]]): Unit = {
    moveList = moveOrFollowers.clone()
  }
}
