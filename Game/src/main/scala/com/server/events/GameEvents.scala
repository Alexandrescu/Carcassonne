package com.server.events

import java.util.UUID

import com.board.Move
import com.corundumstudio.socketio.annotation.OnEvent
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.{Game, GameFactory, Player}
import com.server.json.{GameClient, GameError, GameMove, GameValid}
import com.server.{Converter, PlayerState}
import com.tile.Tile
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class GameEvents(playerState : PlayerState, name : String = "Game") {
  val logger : Logger = Logger(LoggerFactory.getLogger(name))
  val game : Game = GameFactory.standardGame(callEndOfGame)
  private var currentTile : Tile = null
  private var currentPlayer : Player = null

  @OnEvent("playerSessionUpdate")
  def onPlayerSessionUpdate(client: SocketIOClient, player : GameClient): Unit = {
    logger.info("Connecting player")
    playerState.updateUUID(player.slot, player.token, client.getSessionId.toString)
    if(playerState.doneConnecting) {
      val firstMove = game.setBoard()
      client.getNamespace.getBroadcastOperations.sendEvent("gameStart", Converter.moveToJson(firstMove))
      nextRound(client.getNamespace)
    }
  }

  @OnEvent("playerMove")
  def onPlayerMove(client : SocketIOClient, gameMove : GameMove): Unit = {
    if(playerState.isCurrentPlayer(client.getSessionId.toString)) {
      val move : Move = Converter.toMove(gameMove, currentTile, currentPlayer)
      if(game.isMove(move)) {
        game.setMove(move)

        client.sendEvent("GameValid", new GameValid("Move applied."))
        client.getNamespace.getBroadcastOperations.sendEvent("gameMove", Converter.moveToJson(move))

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
    if(finished) {
      endGame(space)
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

  def endGame(space : SocketIONamespace): Unit = {
    game.endGame()
    space.getBroadcastOperations.sendEvent("gameEnd", playerState.endGame)
  }

  def forceStop(): Unit = {
    logger.info("Stopping this game now")
  }
}
