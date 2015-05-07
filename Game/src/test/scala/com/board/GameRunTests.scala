package com.board

import com.game.TileGenerator
import com.player.Player
import com.tile.Section
import org.scalatest.{BeforeAndAfter, FlatSpec, PrivateMethodTester}
import Direction._

class GameRunTests extends FlatSpec with BeforeAndAfter with PrivateMethodTester{

    /*
  1  Left(Tile D at (0,0) facing Up by player Server owning nothing)
  2  Left(Tile J at (1,0) facing Right by player 0 owning 2)
    Right(RemovedFollower @ (1,0), with section 2 and player 0.)
  3  Left(Tile K at (2,0) facing Right by player 1 owning 4)
  4  Left(Tile J at (1,1) facing Up by player 0 owning 2)
  5  Left(Tile Q at (-1,0) facing Right by player 1 owning nothing)
  6  Left(Tile X at (2,-1) facing Right by player 0 owning 7)
  7  Left(Tile U at (1,-1) facing Right by player 1 owning 2)
     */

    val board = new GameBoard(new StandardLogic, new SectionKeeper)
    val D = TileGenerator.D
    val Jm2 = TileGenerator.J
    val K = TileGenerator.K
    val Jm4 = TileGenerator.J
    val Q = TileGenerator.Q
    val X = TileGenerator.X
    val U = TileGenerator.U
    
    val player0 = new Player(0)
    val player1 = new Player(1)

    D.orientation = Up
    val move1 = new Move(D, (0,0), None, null)

    Jm2.orientation = Right
    val move2 = new Move(Jm2, (1,0), Some(Jm2.getSectionById(2)).get, player0)

    K.orientation = Right
    val move3 = new Move(K, (2,0), Some(K.getSectionById(4)).get, player1)

    Jm4.orientation = Up
    val move4 = new Move(Jm4, (1,1), Some(Jm4.getSectionById(2).get), player0)

    Q.orientation = Right
    val move5 = new Move(Q, (-1, 0), None, player1)

    X.orientation = Right
    val move6 = new Move(X, (2, -1), Some(X.getSectionById(7).get), player0)

    U.orientation = Right
    val move7 = new Move(U, (1, -1), Some(U.getSectionById(2).get), player1)

  final type Place = (Int, Int)
  val bOutline = PrivateMethod[Map[Place, Set[Section]]]('boardOutline)
  val tOutline = PrivateMethod[Set[Place]]('tileOutline)

  "Game Run" should "do move 1" in {
    assert(board.isMove(move1))
    board.setMove(move1)

    assert(board.getBoard().size == 1)
    assert(player0.points == 0 && player1.points == 0)
    assert(player0.followers == 7 && player1.followers == 7)
  }

  it should "do move 2" in {
    assert(board.isMove(move2))
    board.setMove(move2)

    assert(board.getBoard().size == 2)
    assert(player0.points == 4 && player1.points == 0)
    assert(player0.followers == 7 && player1.followers == 7)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 4)
  }

  it should "do move 3" in {
    assert(board.isMove(move3))
    board.setMove(move3)

    assert(board.getBoard().size == 3)
    assert(player0.points == 4 && player1.points == 0)
    assert(player0.followers == 7 && player1.followers == 6)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 5)
  }
  it should "do move 4" in {
    assert(board.isMove(move4))
    board.setMove(move4)

    assert(board.getBoard().size == 4)
    assert(player0.points == 4 && player1.points == 0)
    assert(player0.followers == 6 && player1.followers == 6)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 5)
  }
  it should "do move 5" in {
    assert(board.isMove(move5))
    board.setMove(move5)

    assert(board.getBoard().size == 5)
    assert(player0.points == 4 && player1.points == 0)
    assert(player0.followers == 6 && player1.followers == 6)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 8)
  }
  it should "do move 6" in {
    assert(board.isMove(move6))
    board.setMove(move6)

    assert(board.getBoard().size == 6)
    assert(player0.points == 4 && player1.points == 0)
    assert(player0.followers == 5 && player1.followers == 6)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 10)
  }
  it should "do move 7" in {
    assert(board.isMove(move7))
    board.setMove(move7)

    assert(board.getBoard().size == 7)
    assert(player0.points == 4 && player1.points == 0)
    assert(player0.followers == 5 && player1.followers == 5)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 9)
  }
}
