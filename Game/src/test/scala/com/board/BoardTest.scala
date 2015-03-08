package com.board

import com.game.Player
import com.tile._
import org.scalatest.FlatSpec
import com.game.Direction._

class BoardTest extends FlatSpec {
  trait GameCollection {
    val Dsection1 = new GrassSection
    val Dsection2 = new RoadSection
    val Dsection3 = new GrassSection
    val Dsection4 = new CitySection
    Dsection2.addOpen(2)
    Dsection4.addGrass(Set(Dsection3))

    val D = SimpleTile(
      "D",
      RoadEdge(Dsection1, Dsection2, Dsection3),
      RoadEdge(Dsection3, Dsection2, Dsection1),
      GrassEdge(Dsection1),
      CityEdge(Dsection4)
    )

    val Esection1 = new CitySection
    val Esection2 = new GrassSection
    Esection1.addGrass(Set(Esection2))

    val E = SimpleTile("E", CityEdge(Esection1), GrassEdge(Esection2), GrassEdge(Esection2), GrassEdge(Esection2))

    val playerA = new Player {
      override def hasFollower: Boolean = true
      override def name: String = "PlayerA"
    }

    val playerB = new Player {
      override def hasFollower: Boolean = true

      override def name: String = "PlayerB"
    }

    val move1 = new Move(D, (0, 0), None, null)
    val move2 = new Move(E, (1, 0), Some(Esection1), playerA)
  }
  
  trait SimpleBoard {
    val board : Board = new GameBoard(new StandardLogic(), new SectionKeeper())
  }

  "Board" should "contain a collection of the tiles creating the map" in new SimpleBoard with GameCollection {
    assert(board.getBoard().isEmpty)
  }
  it should "know if the move is valid" in new SimpleBoard with GameCollection {
    assert(board.isMove(move1))
  }
  it should "return a valid tile when querying a valid position on the board" in new SimpleBoard with GameCollection {
    board.setMove(move1)
    assert(board.get((0,0)) == Some(D))

    val tile = board.get(0,0).get
    assert(Dsection4.getGrass().size == 1 && Dsection4.openEdges == 1
        && Dsection4.parent == None && Dsection4.tileCount == 1)
  }
  it should "place a move correctly - update the ownership" in new SimpleBoard with GameCollection {
    board.setMove(move1)

    E.orientation = Right
    assert(board.isMove(move2))

    assert(!Dsection4.isOwned)
    board.setMove(move2)
    assert(Dsection4.isOwned && Esection1.isOwned &&
      (Dsection4.owners intersect Esection1.owners) == Set(playerA))

    assert(Dsection4.openEdges == 0 && Esection1.openEdges == 0)
  }
  it should "return the number of points for each player when section is closed"
  it should "evaluate the board points in case of 'game end' event"
}
