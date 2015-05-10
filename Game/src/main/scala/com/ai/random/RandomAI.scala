package com.ai.random

import com.ai.AI
import com.board.{RemovedFollower, Move, PossibleMove}
import com.client.Client
import com.server.Converter
import com.server.json.GameMove
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class RandomAI(client : Client) extends AI {
  val random = new Random()
  override def getMove(tile : Tile, moves: Set[PossibleMove]): GameMove = {
    val movePicked = moves.toVector(random.nextInt(n = moves.size))
    val sectionPicked = movePicked.toOwnFromTile.toVector(random.nextInt(movePicked.toOwnFromTile.size))

    val sectionId = sectionPicked match {
      case Some(section) => section.frontEndId
      case None => -1
    }

    new GameMove(movePicked.place._1, movePicked.place._2, Converter.directionToString(movePicked.direction), sectionId)
  }

  override def movePlayed(moveOrFollower: Either[Move, RemovedFollower]): Unit = {}

  override def currentState(moveOrFollowers: ArrayBuffer[Either[Move, RemovedFollower]]): Unit = {}
}
