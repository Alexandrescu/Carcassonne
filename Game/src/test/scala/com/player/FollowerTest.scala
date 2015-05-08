package com.player

import com.board._
import com.game.TileGenerator
import org.scalatest.FlatSpec

import scala.collection.mutable.ArrayBuffer

class FollowerTest extends FlatSpec {
  class FightBoard(val sharedMaj : Boolean) {
    val board = new GameBoard(new StandardLogic, new SectionKeeper)

    val player0 = new Player(0)
    val player1 = new Player(1)
    val player2 = new Player(2)

    val moves : ArrayBuffer[Move] = new ArrayBuffer()
    moves += new Move(TileGenerator.B, (0,0), None, null)


    val Us = (for(i <- 0 to 3) yield TileGenerator.U).toArray

    Us(0).orientation = Direction.Right
    Us(1).orientation = Direction.Right
    moves += new Move(Us(0), (0, 1), Us(0).getSectionById(2), player0)
    moves += new Move(Us(1), (0, -1), Us(1).getSectionById(2), player0)
    
    moves += new Move(Us(2), (-1, 0), Us(2).getSectionById(2), player1)
    if(sharedMaj) {
      moves += new Move(Us(3), (1, 0), Us(3).getSectionById(2), player1)
    }
    else {
      moves += new Move(Us(3), (1, 0), Us(3).getSectionById(2), player2)
    }

    // Closing the road
    val Vs = (for(i <- 0 to 3) yield TileGenerator.V).toArray
    // Top right
    moves += new Move(Vs(0), (1, 1), None, player0)
    // Top left
    Vs(1).orientation = Direction.Right
    moves += new Move(Vs(1), (-1, 1), None, player1)
    // Bottom left
    Vs(2).orientation = Direction.Down
    moves += new Move(Vs(2), (-1, -1), None, player2)
    //Bottom right
    Vs(3).orientation = Direction.Left
    moves += new Move(Vs(3), (1, -1), None, player0)
  }
  
  "Followers" should "give player 0, 8 points when no maj" in new FightBoard(false) {
    for(move <- moves) {
      assert(player0.points == 0 && player1.points == 0 && player2.points == 0)
      assert(board.isMove(move))
      board.setMove(move)
    }

    assert(player0.points == 8 && player1.points == 0 && player2.points == 0)
    assert(player0.followers == 7 && player1.followers == 7 && player2.followers == 7)
  }

  it should "give player 0 and player 1, 8 points when maj" in new FightBoard(true) {
    for(move <- moves) {
      assert(player0.points == 0 && player1.points == 0 && player2.points == 0)
      assert(board.isMove(move))
      board.setMove(move)
    }

    assert(player0.points == 8 && player1.points == 8 && player2.points == 0)
    assert(player0.followers == 7 && player1.followers == 7 && player2.followers == 7)
  }
}
