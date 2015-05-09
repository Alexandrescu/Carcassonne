package com.client

import com.board.{Move, PossibleMove, RemovedFollower}
import com.corundumstudio.socketio.SocketIOClient
import com.player.Player
import com.server.Converter
import com.server.json.{GameClientPlayer, GameInfo}
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

class RealClient(private val _slot : Int, private val _token : String, private val _name : String) extends Client{
  /* Initializations */
  override def slot: Int = _slot

  def token: String = _token

  override def name: String = _name

  private val _player = new Player(slot)
  override def player: Player = _player

  /* State methods */
  override def turn(tile: Tile, moves : Set[PossibleMove]): Unit = {
    socketClient.sendEvent("gameNext", Converter.toGameNextMoveList(tile, moves))
  }

  override def currentState(moves: ArrayBuffer[Either[Move, RemovedFollower]]): Unit = {
    for(move <- moves) move match {
      case Left(m) => socketClient.sendEvent("gameMove", Converter.toGameMove(m))
      case Right(f) => socketClient.sendEvent("followerRemoved", Converter.toGameRemoveFollower(f))
    }
  }

  /* Socket state update */
  private var client : Option[SocketIOClient] = None

  def socketClient: SocketIOClient = {
    if(client.isEmpty) throw UninitializedFieldError("Client socket has not been initialised.")
    client.get
  }

  def socketClient_=(newClient: SocketIOClient): Unit = {
    client = Some(newClient)
  }

  override def movePlayed(move: Either[Move, RemovedFollower]): Unit = {
  }

  override def playedMoveInfo(valid: Boolean): Unit = {
    if(valid) {
      socketClient.sendEvent("GameValid", new GameInfo("Move applied."))
    }
    else {
      socketClient.sendEvent("GameError", new GameInfo("Not a valid move."))
    }
  }

  /* End of game results */
  override def endGame(summary: List[GameClientPlayer]): Unit = {}

  /* Inform on a draw */
  override def draw(currentTile: Tile, player: Player): Unit = {}
}
