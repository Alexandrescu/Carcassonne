package com.board

import com.game.Game
import org.scalatest.FlatSpec

class GameTest extends FlatSpec {

  val mockGame = new Game

  "com.game.Game" should "know how many player have connected to the game"
  it should "be able to identify players by id" in {
    // In here I will mock the player id inside the game
  }

  it should "assign a move to the respective player"
  it should "ignore any move from another player"
  it should "reject an invalid move from the respective player"
  it should "accept the move from the respective player"
}
