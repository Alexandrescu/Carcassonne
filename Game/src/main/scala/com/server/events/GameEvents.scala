package com.server.events

import com.board.Move
import com.corundumstudio.socketio.annotation.{OnConnect, OnEvent}
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.Game
import com.server.json.{GameClient, GameError, GameMove, GameValid}
import com.server.{ClientState, Converter}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class GameEvents(val game : Game, name : String = "Game") {
  val logger : Logger = Logger(LoggerFactory.getLogger(name))

  @OnConnect
  def onConnect(client : SocketIOClient): Unit = {
    logger.info("Client has connected.")

    /* SEND INFO TO CLIENT

    logger.info(s"Move queue size is: ${moveQueue.size}")

    if(moveQueue.length > 0) {
      for(move <- moveQueue) {
        client.sendEvent("gameMove", move)
      }
    }
    client.sendEvent("gameDraw", Converter.toGameDraw(currentTile, currentPlayer))

    */
  }

  private val clientState : ClientState = new ClientState()
  @OnEvent("connectAs")
  def onPlayerSessionUpdate(client: SocketIOClient, player : GameClient): Unit = {
    logger.info(s"Connecting as player on slot: ${player.slot}.")
    val gameClient = clientState.setUUID(player.slot, player.token, client.getSessionId.toString)

    if(game.isCurrentPlayer(gameClient.player)) {
      // Send him the tile to move
      //client.sendEvent("gameNext", Converter.toGameNextMoveList(game.currentTile, game.currentMoves))
    }
  }

  @OnEvent("playerMove")
  def onPlayerMove(client : SocketIOClient, gameMove : GameMove): Unit = {
    val gameClient = clientState.get(client.getSessionId.toString)
    if(game.isCurrentPlayer(gameClient.player)) {
      val move : Move = Converter.toMove(gameMove, game.currentTile, gameClient.player)

      if(game.isMove(move)) {
        game.setMove(move)

        client.sendEvent("GameValid", new GameValid("Move applied."))

        // This is essential, because I am keeping info only on the server side
        val newMove = Converter.moveToJson(move)
        client.getNamespace.getBroadcastOperations.sendEvent("gameMove", newMove)

        nextRound(client.getNamespace)
      }
      else {
        client.sendEvent("GameError", new GameError("Not a valid move."))
      }
    }
    else {
      client.sendEvent("GameError", new GameError("Not your turn."))
    }
  }

  def nextRound(space : SocketIONamespace) : Unit = {
    if(game finished) {
      // End the game.
      return
    }
    game.next
  }
}
