package com.board

import com.game.TileGenerator
import com.player.Player
import com.tile._
import org.scalatest.FlatSpec
import Direction._

class BoardTest extends FlatSpec {
  trait GameCollection {
    val D = TileGenerator.D
    val E = TileGenerator.E

    val playerA = new Player(0)
    val playerB = new Player(1)

    val D4 : CitySection = D.getSectionById(4).get match {
      case c : CitySection => c
      case _ => fail("Section should be city section.")
    }

    val E2 : CitySection = E.getSectionById(2) get match {
      case c : CitySection => c
      case _ => fail("Sections should be a grass section.")
    }

    val move1 = new Move(D, (0, 0), None, null)

    E.orientation = Right
    val move2 = new Move(E, (1, 0), E.getSectionById(2), playerA)
  }
  
  trait StandardBoard {
    val board : Board = new GameBoard(new StandardLogic(), new SectionKeeper())
  }

  trait MiniGame {
    val board : Board = new GameBoard(new StandardLogic(), new SectionKeeper())
    board.setMove(new Move(TileGenerator.D, (0, 0), None, null))

    // The board is set now the tiles
    val A = TileGenerator.A
    val B = TileGenerator.B
    val L = TileGenerator.L
    val J = TileGenerator.J
    val O = TileGenerator.O
    val U = TileGenerator.U
    val Q = TileGenerator.Q

    val Vtiles = for(i <- 0 to 3) yield TileGenerator.V
    val W = TileGenerator.W

    val playerA = new Player(0)
    val playerB = new Player(1)
    val playerC = new Player(2)

    val Q1 : CitySection = Q.getSectionById(1).get match {
      case c : CitySection => c
      case _ => fail("Should be a city section.")
    }

    val L7 : CitySection = L.getSectionById(7).get match {
      case c : CitySection => c
      case _ => fail("Should be a city section.")
    }
  }

  "Board" should "contain a collection of the tiles creating the map" in new StandardBoard with GameCollection {
    assert(board.getBoard().isEmpty)
  }
  it should "know if the move is valid" in new StandardBoard with GameCollection {
    assert(board.isMove(move1))
  }
  it should "return a valid tile when querying a valid position on the board" in new StandardBoard with GameCollection {
    board.setMove(move1)
    assert(board.get((0,0)) == Some(D))
    //assert(D4.getGrass().size == 1 && D4.openEdges == 1 && D4.tileCount == 1)
  }
  it should "place a move correctly - update the ownership" in new StandardBoard with GameCollection {
    // Placing the initial tile
    board.setMove(move1)
    assert(!D4.isOwned)

    // Applying the next move
    assert(board.isMove(move2))
    assert(!D4.isOwned)

    board.setMove(move2)
    assert(D4.isOwned && E2.isOwned &&
      (D4.followers intersect E2.followers).map(f => f.player) == Set(playerA))

    //assert(D4.openEdges == 0 && E2.openEdges == 0)
  }
  it should "return the number of points for each player when city section is closed" in new StandardBoard with GameCollection{
    board.setMove(move1)
    board.setMove(move2)

    assert(playerA.points == 4 && playerB.points == 0)
  }

  it should "get available moves" in new MiniGame {
    val moves1 = board.getMoves(J, playerA)
    assert(moves1.toSeq.map(_.toOwnFromTile.size).sum == 30)

    val moves2 = board.getMoves(L, playerA)
    assert(moves2.toSeq.map(_.toOwnFromTile.size).sum == 56)

    val moves3 = board.getMoves(B, playerA)
    assert(moves3.toSeq.map(_.toOwnFromTile.size).sum == 12)
  }
  it should "know how to union cities" in new MiniGame {
    val move1 = new Move(L, (0, -1), Some(L7), playerA)
    Q.orientation = Right
    val move2 = new Move(Q, (1, 0), Some(Q1), playerB)
    J.orientation = Down
    val move3 = new Move(J, (1, 1), None, playerA)
    val move4 = new Move(O, (1, -1), None, playerB)

    board.setMove(move1)
    assert(L7.followers.map(f => f.player).contains(playerA))

    board.setMove(move2)
    assert(Q1.followers.map(f => f.player).contains(playerB))

    board.setMove(move3)

    board.setMove(move4)
    assert(Q1.followers.map(f => f.player).contains(playerA)
        && Q1.followers.map(f => f.player).contains(playerB))

    assert(playerB.points == 14 && playerA.points == 14)
  }
}
