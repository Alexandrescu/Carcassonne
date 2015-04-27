package com.server.events

import java.util.UUID

import com.board.{Move, PossibleMove}
import com.corundumstudio.socketio.annotation.{OnConnect, OnEvent}
import com.corundumstudio.socketio.{SocketIOClient, SocketIONamespace}
import com.game.{Game, GameFactory, Player}
import com.server.json.{GameClient, GameError, GameMove, GameValid}
import com.server.{Converter, PlayerState}
import com.tile.Tile
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer

class GameEvents(playerState : PlayerState, name : String = "Game") {
  val logger : Logger = Logger(LoggerFactory.getLogger(name))
  //val game : Game = GameFactory.standardGame(callEndOfGame)
  val game : Game = GameFactory.testGame(callEndOfGame)
  private var currentTile : Tile = null
  private var currentPlayer : Player = null
  private var currentMoves : Set[PossibleMove] = null

  private var moveQueue : ArrayBuffer[GameMove] = ArrayBuffer()

  @OnConnect
  def onConnect(client : SocketIOClient): Unit = {
    logger.info("Client has connected.")
    logger.info(s"Move queue size is: ${moveQueue.size}")

    if(moveQueue.length > 0) {
      for(move <- moveQueue) {
        client.sendEvent("gameMove", move)
      }
    }

    client.sendEvent("gameDraw", Converter.toGameDraw(currentTile, currentPlayer))
  }

  @OnEvent("playerSessionUpdate")
  def onPlayerSessionUpdate(client: SocketIOClient, player : GameClient): Unit = {
    logger.info("Connecting player")
    playerState.updateUUID(player.slot, player.token, client.getSessionId.toString)

    if(playerState.doneConnecting) {
      logger.info("Commencing the game.")
      val firstMove = Converter.moveToJson(game.setBoard())
      moveQueue += firstMove
      client.getNamespace.getBroadcastOperations.sendEvent("gameStart", firstMove)
      nextRound(client.getNamespace)
    }
    else if(playerState.isCurrentPlayer(client.getSessionId.toString)) {
      // Send him the tile to move
      client.sendEvent("gameNext", Converter.toGameNextMoveList(currentTile, currentMoves))
    }
  }

  @OnEvent("playerMove")
  def onPlayerMove(client : SocketIOClient, gameMove : GameMove): Unit = {
    if(playerState.isCurrentPlayer(client.getSessionId.toString)) {
      val move : Move = Converter.toMove(gameMove, currentTile, currentPlayer)
      if(game.isMove(move)) {
        game.setMove(move)

        logger.info(s"Played move: $move")
        client.sendEvent("GameValid", new GameValid("Move applied."))

        // This is essential, because I am keeping info only on the server side
        val newMove = Converter.moveToJson(move)
        moveQueue += newMove

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
    if(finished) {
      endGame(space)
      return
    }
    currentTile = game.drawTile()
    currentPlayer = playerState.nextPlayer
    currentMoves = game.getMoves(currentTile, currentPlayer)

    sendNextMove(space)
  }

  def sendNextMove(space: SocketIONamespace): Unit = {
    space.getClient(UUID.fromString(currentPlayer.uuid)).
      sendEvent("gameNext", Converter.toGameNextMoveList(currentTile, currentMoves))
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
