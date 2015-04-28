package com.game

import com.board.{GameBoard, Move}
import com.client.Client
import com.corundumstudio.socketio.SocketIOClient
import com.player.Player
import com.server.json.GameClient
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

class Game(board : GameBoard, tileBag : TileBag, clientTurn: ClientTurn) {
  private var moveQueue : ArrayBuffer[Move] = ArrayBuffer(new Move(tileBag.startTile, (0, 0), None, null))
  board.setMove(moveQueue(0))

  /*
      PRE: Game not ended
      POST: Game in the next stage: tile removed, player informed
   */
  def next: Unit = {
    // MAYBE CHECK IF THE GAME IS DONE
    val gameClient = clientTurn.next()
    val currentTile = tileBag.next()
    gameClient.turn(currentTile, board.getMoves(currentTile, gameClient.player))
  }

  /* State handlers */
  def currentTile : Tile = tileBag.current

  def finished: Boolean = tileBag.hasNext

  def moveList : List[Move] = moveQueue.clone().toList

  def isMove(move : Move): Boolean = {
    board.isMove(move)
  }

  def setMove(move : Move): Unit = {
    //logger.info(s"Played move: $move.")
    board.setMove(move)
    moveQueue += move
  }

  /* Informing a client when connecting to the game */
  def informClient(gameClient : Client): Unit = {
    gameClient.currentState(moveQueue.clone())
  }

  private var started = false
  def updateClient(client : SocketIOClient, info : GameClient): Unit = {
    val thisClient = clientTurn.updateClient(client, info)
    if(!started && clientTurn.doneConnecting) {
      started = true
      next
    }
    if(isCurrentPlayer(thisClient)) {
      val thisTile = tileBag.current
      thisClient.turn(thisTile, board.getMoves(thisTile, thisClient.player))
    }
  }

  def getClient(client : SocketIOClient) : Client = clientTurn.current

  def isCurrentPlayer(gameClient : Client): Boolean = clientTurn.current == gameClient

  def currentPlayer : Player = clientTurn.current.player
}
