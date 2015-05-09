package com.client

import com.board.{Move, PossibleMove, RemovedFollower}
import com.player.Player
import com.server.json.GameClientPlayer
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

trait Client {
  def slot : Int
  def token : String
  def name : String

  /* Turn is my turn */
  def turn(tile : Tile, moves : Set[PossibleMove]) : Unit
  /* Callback on the move, if something happened */
  def playedMoveInfo(valid : Boolean)
  /* Current state is when you connect, might be able to remove this */
  def currentState(moves: ArrayBuffer[Either[Move, RemovedFollower]]) : Unit
  /* What player it has in the game */
  def player : Player

  var connected : Boolean = false

  /* Move that someone played */
  def movePlayed(move : Either[Move, RemovedFollower])

  /* End of game results */
  def endGame(summary : List[GameClientPlayer])

  /* Inform on a draw */
  def draw(currentTile : Tile, player : Player)
}
