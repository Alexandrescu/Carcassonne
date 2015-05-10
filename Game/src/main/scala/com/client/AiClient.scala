package com.client

import com.ai.uct.AiUct
import com.ai.{AI, AiFlag}
import com.ai.random.RandomAI
import com.board.{PossibleMove, RemovedFollower, Move}
import com.game.Game
import com.player.Player
import com.server.json.GameClientPlayer
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

class AiClient(flag : Int, private val _slot : Int, private val _token : String, private val _name : String) extends Client{
  var game : Option[Game] = None

  val ai : AI = flag match {
    case AiFlag.Random => new RandomAI(this)
    case AiFlag.MC => new AiUct(this, 2)
    case _ => new RandomAI(this)
  }

  private val _player = new Player(slot)
  override def player: Player = _player

  /* Turn is my turn */
  override def turn(tile: Tile, moves: Set[PossibleMove]): Unit = {
    game.get.aiMove(ai.getMove(tile, moves), this)
  }

  /* Current state is when you connect, might be able to remove this */
  override def currentState(moves: ArrayBuffer[Either[Move, RemovedFollower]]): Unit = {
    ai.currentState(moves)
  }

  /* Inform on a draw */
  override def draw(currentTile: Tile, player: Player): Unit = {}

  /* Move that someone played */
  override def movePlayed(move: Either[Move, RemovedFollower]): Unit = {
    ai.movePlayed(move)
  }

  /* Callback on the move, if something happened */
  override def playedMoveInfo(valid: Boolean): Unit = {}

  /* End of game results */
  override def endGame(summary: List[GameClientPlayer]): Unit = {}

  override def slot: Int = _slot

  override def name: String = _name

  def registerGame(game: Game): Unit = {
    this.game = Some(game)
  }
}
