package com.client

import com.board.{Move, PossibleMove, RemovedFollower}
import com.corundumstudio.socketio.SocketIOClient
import com.player.Player
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

class AiClient extends Client{
  override def slot: Int = ???

  override def token: String = ???

  override def name: String = ???

  override def player: Player = ???

  override def socketClient: SocketIOClient = ???

  override def socketClient_=(socketIOClient: SocketIOClient): Unit = ???

  override def turn(tile: Tile, moves: Set[PossibleMove]): Unit = ???

  override def currentState(moves: ArrayBuffer[Either[Move, RemovedFollower]]): Unit = ???

  override def movePlayed(move: Either[Move, RemovedFollower]): Unit = ???
}
