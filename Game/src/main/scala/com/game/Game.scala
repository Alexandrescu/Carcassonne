package com.game

import com.board.{GameBoard, Move}
import com.client.Client
import com.player.Player
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

class Game(board : GameBoard, tileBag : TileBag, playerTurns: PlayerTurns) {
  private var moveQueue : ArrayBuffer[Move] = ArrayBuffer(new Move(tileBag.startTile, (0, 0), None, null))
  board.setMove(moveQueue(0))

  def currentTile : Tile = tileBag.current

  /*
      PRE: Game not ended
      POST: Game in the next stage: tile removed, player informed
   */
  def next: Unit = {
    // MAYBE CHECK IF THE GAME IS DONE
    playerTurns.next(tileBag.next())
  }

  def finished: Boolean = tileBag.hasNext

  def isCurrentPlayer(player: Player): Boolean = playerTurns.current == player

  def isMove(move : Move): Boolean = {
    board.isMove(move)
  }

  def setMove(move : Move): Unit = {
    //logger.info(s"Played move: $move.")
    board.setMove(move)
    moveQueue += move
  }

  def informClient(gameClient : Client): Unit = {
    gameClient.currentState(moveQueue.clone())
  }
}
