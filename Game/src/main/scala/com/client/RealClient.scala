package com.client

import com.board.{PossibleMove, Move}
import com.corundumstudio.socketio.SocketIOClient
import com.player.{Follower, Player}
import com.server.Converter
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

class RealClient(private val _slot : Int, private val _token : String, private val _name : String) extends Client{
  /* Initializations */
  override def slot: Int = _slot

  override def token: String = _token

  override def name: String = _name

  private val _player = new Player(this, slot)
  override def player: Player = _player

  /* State methods */
  override def turn(tile: Tile, moves : Set[PossibleMove]): Unit = {
    socketClient.sendEvent("gameNext", Converter.toGameNextMoveList(tile, moves))
  }

  override def currentState(moves: ArrayBuffer[Move]): Unit = {
    for(move <- moves) {
      socketClient.sendEvent("gameMove", Converter.toGameMove(move))
    }
  }

  /* Socket state update */
  private var client : Option[SocketIOClient] = None
  override def socketClient: SocketIOClient = {
    if(client.isEmpty) throw UninitializedFieldError("Client socket has not been initialised.")
    client.get
  }

  override def socketClient_=(newClient: SocketIOClient): Unit = {
    client = Some(newClient)
  }

  /* PlayerObserver updates */
  override def playerUpdate(player: Player): Unit = {
    socketClient.getNamespace.getBroadcastOperations.sendEvent("playerUpdate", Converter.toGamePlayer(_player))
  }

  override def followerUpdate(follower: Follower): Unit = {
    socketClient.getNamespace.getBroadcastOperations.sendEvent("followerRemoved",
      Converter.toGameRemoveFollower(follower.removedPlace, follower.removedFrontEndId))
    socketClient.getNamespace.getBroadcastOperations.sendEvent("playerUpdate", Converter.toGamePlayer(_player))
  }
}
