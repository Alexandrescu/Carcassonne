package com.ai

import com.board.{RemovedFollower, Move, PossibleMove}
import com.server.json.GameMove
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

trait AI {
  def movePlayed(moveOrFollower: Either[Move, RemovedFollower]): Unit

  def currentState(moveOrFollowers: ArrayBuffer[Either[Move, RemovedFollower]]): Unit

  def getMove(tile : Tile, moves: Set[PossibleMove]) : GameMove
}
