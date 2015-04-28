package com.server.events

import com.board.Move
import com.corundumstudio.socketio.annotation.{OnConnect, OnEvent}
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.Game
import com.server.Converter
import com.server.json.{GameClient, GameError, GameMove, GameValid}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class GameEvents(val game : Game, name : String = "Game Event") {
  val logger : Logger = Logger(LoggerFactory.getLogger(name))

  @OnConnect
  def onConnect(client : SocketIOClient): Unit = {
    logger.info("Spectator has connected.")

    /* SEND INFO TO CLIENT */
    for(move <- game.moveList) {
      client.sendEvent("gameMove", Converter.toGameMove(move))
    }

    /* Send current turn information */
    if(game started) {
      client.sendEvent("gameDraw", Converter.toGameDraw(game.currentTile, game.currentPlayer))
    }
  }

  @OnEvent("connectAs")
  def onPlayerSessionUpdate(client: SocketIOClient, player : GameClient): Unit = {
    logger.info(s"Connecting as player on slot: ${player.slot}.")
    game.updateClient(client, player)
  }

  @OnEvent("playerMove")
  def onPlayerMove(client : SocketIOClient, gameMove : GameMove): Unit = {
    val gameClient = game.getClient(client)
    if(game.isCurrentPlayer(gameClient)) {
      val move : Move = Converter.toMove(gameMove, game.currentTile, gameClient.player)

      if(game.isMove(move)) {
        game.setMove(move)

        client.sendEvent("GameValid", new GameValid("Move applied."))

        // This is essential, because I am keeping info only on the server side
        val newMove = Converter.toGameMove(move)
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
    game.next()
  }
}
