package com.server

import java.util.UUID

import com.board.Move
import com.corundumstudio.socketio.annotation.{OnConnect, OnEvent}
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.{Player, Game, GameFactory}
import com.server.json._
import com.tile.Tile
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class GameSpace(space : SocketIONamespace, playerState : PlayerState) {
  val logger = Logger(LoggerFactory.getLogger("Carcassonne Game"))
  val game : Game = GameFactory.standardGame(callEndOfGame)

  private var currentTile : Tile = null
  private var currentPlayer : Player = null

  class GameEvents {
    @OnConnect
    def onConnect(client: SocketIOClient): Unit = {
      logger.info(s"Another client has connected ${client.getSessionId.toString}")
    }

    @OnEvent("playerSessionUpdate")
    def onPlayerSessionUpdate(client: SocketIOClient, player : GameClient): Unit = {
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
        val move : Move = Converter.toMove(gameMove, currentTile, currentPlayer)
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

  space.addListeners(new GameEvents)

  def nextRound {
    if(finished) {
      endGame()
      return
    }
    currentTile = game.drawTile()
    currentPlayer = playerState.nextPlayer
    val availableMoves = game.getMoves(currentTile, currentPlayer)

    space.getClient(UUID.fromString(currentPlayer.uuid)).
      sendEvent("gameNext", Converter.toGameNextMoveList(currentTile, availableMoves))
    space.getBroadcastOperations.sendEvent("gameDraw", Converter.toGameDraw(currentTile, currentPlayer))
  }

  private var finished = false
  def callEndOfGame(): Unit = {
    finished = true
  }

  def endGame(): Unit = {
    game.endGame()
    space.getBroadcastOperations.sendEvent("gameEnd", playerState.endGame)
  }

  def forceStop: Unit = {
    logger.info("Stopping this game now")
  }
}
