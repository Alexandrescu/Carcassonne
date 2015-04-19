package com.server

import java.util.UUID

import com.board.Move
import com.corundumstudio.socketio.annotation.{OnConnect, OnEvent}
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.{Game, GameFactory}
import com.server.json._
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class GameSpace(space : SocketIONamespace, playerState : PlayerState) {
  val logger = Logger(LoggerFactory.getLogger("Carcassonne Game"))
  val game : Game = GameFactory.standardGame(callEndOfGame)

  class GameEvents {
    @OnConnect
    def onConnect(client: SocketIOClient): Unit = {
      logger.info(s"Another client has connected ${client.getSessionId.toString}")
    }

    @OnEvent("playerSessionUpdate")
    def onPlayerSessionUpdate(client: SocketIOClient, player : GamePlayer): Unit = {
      playerState.updateUUID(player.slot, player.token, client.getSessionId.toString)
      if(playerState.doneConnecting) {
        val firstMove = game.setBoard()
        space.getBroadcastOperations.sendEvent("gameStart", Converter.moveToJson(firstMove))
        nextRound
      }
    }

    @OnEvent("playerMove")
    def onPlayerMove(client : SocketIOClient, gameMove : GameMove): Unit = {
      if(playerState.isCurrentPlayer(client.getSessionId.toString)) {
        val move : Move = Converter.toMove(gameMove)
        if(game.isMove(move)) {
          game.setMove(move)

          client.sendEvent("GameValid", new GameValid("Move applied."))
          space.getBroadcastOperations.sendEvent("gameMove", Converter.moveToJson(move))

          nextRound
        }
        else {
          client.sendEvent("GameError", new GameError("Not a valid move."))
        }
      }
      else {
        client.sendEvent("GameError", new GameError("Not your turn."))
      }
    }
  }

  def nextRound {
    if(finished) {
      endGame()
      return
    }
    val nextTile = game.drawTile()
    val nextPlayer = playerState.nextPlayer
    val availableMoves = game.getMoves(nextTile, nextPlayer)

    space.getClient(UUID.fromString(nextPlayer.uuid)).
      sendEvent("gameNext", Converter.toGameNextMove(nextTile, availableMoves))
    space.getBroadcastOperations.sendEvent("gameDraw", Converter.toGameDraw(nextTile, nextPlayer))
  }

  private var finished = false
  def callEndOfGame(): Unit = {
    finished = true
  }

  def endGame(): Unit = {
    game.endGame()
    space.getBroadcastOperations.sendEvent("gameEnd", playerState.endGame)
  }

  def forceStop = ???
}
