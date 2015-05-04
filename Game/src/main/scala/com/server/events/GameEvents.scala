package com.server.events

import com.board.Move
import com.corundumstudio.socketio.annotation.{OnConnect, OnEvent}
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.Game
import com.server.Converter
import com.server.json._
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class GameEvents(val game : Game, name : String = "Game Event") {
  val logger : Logger = Logger(LoggerFactory.getLogger(name))

  @OnConnect
  def onConnect(client : SocketIOClient): Unit = {
    logger.info("Spectator has connected.")

    /* SEND INFO TO CLIENT */
    for(move <- game.moveList) move match {
      case Left(m) => client.sendEvent("gameMove", Converter.toGameMove(m))
      case Right(f) => client.sendEvent("followerRemoved", Converter.toGameRemoveFollower(f))
    }

    /* Send current turn information */
    if(game started) {
      client.sendEvent("gameDraw", Converter.toGameDraw(game.currentTile, game.currentPlayer))
    }
    client.sendEvent("gameSlots", new GameSlots(game.getSlots.toList))
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
        val newMoveList = game.setMove(move)

        client.sendEvent("GameValid", new GameValid("Move applied."))

        // Telling people what has happened
        for(move <- newMoveList) move match {
          case Left(m) =>
            client.getNamespace.getBroadcastOperations.sendEvent("gameMove", Converter.toGameMove(m))
          case Right(f) =>
            client.getNamespace.getBroadcastOperations.sendEvent("followerRemoved", Converter.toGameRemoveFollower(f))
        }

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
