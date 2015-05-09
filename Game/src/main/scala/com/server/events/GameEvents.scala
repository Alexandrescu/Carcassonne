package com.server.events

import com.board.Move
import com.client.Client
import com.corundumstudio.socketio.annotation.{OnConnect, OnEvent}
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.Game
import com.server.Converter
import com.server.json._
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class GameEvents(val game : Game, val namespace: SocketIONamespace, val name : String = "Game Event") {
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
    if(game finished) {
      client.sendEvent("gameEnd", new GameSlots(game.getSlots.toList))
    }
    else {
      if(game started) {
        client.sendEvent("gameDraw", Converter.toGameDraw(game.currentTile, game.currentPlayer))
      }
      client.sendEvent("gameSlots", new GameSlots(game.getSlots.toList))
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
    runMove(gameMove, gameClient)
  }

  def nextRound() : Unit = {
    if(game finished) {
      namespace.getBroadcastOperations.sendEvent("gameEnd", new GameSlots((game summary).toList))
      return
    }
    game.next()
  }

  def runMove(gameMove : GameMove, client : Client): Unit = {
    if(game.isCurrentPlayer(client)) {
      val move : Move = Converter.toMove(gameMove, game.currentTile, client.player)
      if(game.isMove(move)) {
        val newMoveList = game.setMove(move)

        client.playedMoveInfo(valid = true)

        // Telling people what has happened
        for(move <- newMoveList) move match {
          case Left(m) =>
            namespace.getBroadcastOperations.sendEvent("gameMove", Converter.toGameMove(m))
            namespace.getBroadcastOperations.sendEvent("gameSlots", new GameSlots(game.getSlots.toList))
          case Right(f) =>
            namespace.getBroadcastOperations.sendEvent("followerRemoved", Converter.toGameRemoveFollower(f))
            namespace.getBroadcastOperations.sendEvent("gameSlots", new GameSlots(game.getSlots.toList))
        }

        nextRound()
      }
      else {
        client.playedMoveInfo(valid = false)
      }
    }
    else {
      client.playedMoveInfo(valid = false)
    }

  }
}
