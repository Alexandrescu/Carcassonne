package com.board

import com.player.Player
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

  trait MiniGame {
    val Asect1 = new GrassSection
    val Asect2 = new RoadSection
    val Asect3 = new MonasterySection
    val A = Monastery("A", GrassEdge(Asect1), RoadEdge(Asect1, Asect2, Asect1), GrassEdge(Asect1), GrassEdge(Asect1), Asect3)

    val Bsect1 = new GrassSection
    val Bsect2 = new MonasterySection
    val B = Monastery("B", GrassEdge(Bsect1), GrassEdge(Bsect1), GrassEdge(Bsect1), GrassEdge(Bsect1), Bsect2)

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

    val board : Board = new GameBoard(new StandardLogic(), new SectionKeeper())
    board.setMove(new Move(D, (0, 0), None, null))

    // The board is set now the tiles
    val Lsection1 = new GrassSection
    val Lsection2 = new RoadSection
    val Lseciton3 = new GrassSection

    val Lsection4 = new GrassSection

    val Lsection5 = new RoadSection

    val Lsection6 = new CitySection

    val L = SimpleTile(
    "L", RoadEdge(Lsection1, Lsection2, Lseciton3), RoadEdge(Lseciton3, Lsection2, Lsection4),
         RoadEdge(Lsection4, Lsection5, Lsection1), CityEdge(Lsection6)
    )

    val Jsect1 = new CitySection
    val Jsect2 = new GrassSection
    val Jsect3 = new RoadSection
    val Jsect4 = new GrassSection
    val J = SimpleTile("J", CityEdge(Jsect1), RoadEdge(Jsect4, Jsect3, Jsect2), GrassEdge(Jsect2), RoadEdge(Jsect2, Jsect3, Jsect4))

    val Osect1 = new CitySection
    val Osect2 = new GrassSection
    val Osect3 = new RoadSection
    val Osect4 = new GrassSection
    val O = SimpleTile("O", CityEdge(Osect1), RoadEdge(Osect4, Osect3, Osect2), CityEdge(Osect1), RoadEdge(Osect2, Osect3, Osect4))

    val Usect1 = new GrassSection
    val Usect2 = new RoadSection
    val Usect3 = new GrassSection
    val U = SimpleTile("U", RoadEdge(Usect1, Usect2, Usect3), RoadEdge(Usect3, Usect2, Usect1), GrassEdge(Usect1), GrassEdge(Usect3))

    val Qsect1 = new CitySection
    val Qsect2 = new GrassSection
    val Q = SimpleTile("Q", CityEdge(Qsect1), GrassEdge(Qsect2), CityEdge(Qsect1), CityEdge(Qsect1))

    val Vsect1 = Array(new GrassSection, new GrassSection, new GrassSection, new GrassSection)
    val Vsect2 = for(i <- 0 to 3) yield (new RoadSection)
    val Vsect3 = Array(new GrassSection, new GrassSection, new GrassSection, new GrassSection)

    val Vtiles = for(i <- 0 to 3) yield SimpleTile("V",
      GrassEdge(Vsect1(i)), RoadEdge(Vsect1(i), Vsect2(i), Vsect3(i)),
      RoadEdge(Vsect3(i), Vsect2(i), Vsect1(i)), GrassEdge(Vsect1(i))
    )

    val Wsect1 = new GrassSection
    val Wsect2 = new GrassSection
    val Wsect3 = new GrassSection

    val Wsect4 = new RoadSection
    val Wsect5 = new RoadSection
    val Wsect6 = new RoadSection

    val W = SimpleTile("W", GrassEdge(Wsect1), RoadEdge(Wsect3, Wsect5, Wsect2),
      RoadEdge(Wsect2, Wsect4, Wsect1), RoadEdge(Wsect1, Wsect6, Wsect3))

    val playerA = new Player {
      override def name: String = "PlayerA"
    }

    val playerB = new Player {
      override def name: String = "PlayerB"
    }

    val playerC = new Player {
      override def name: String = "PlayerC"
    }
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
        && Dsection4.tileCount == 1)
  }
  it should "place a move correctly - update the ownership" in new SimpleBoard with GameCollection {
    board.setMove(move1)

    E.orientation = Right
    assert(board.isMove(move2))

    assert(!Dsection4.isOwned)
    board.setMove(move2)
    assert(Dsection4.isOwned && Esection1.isOwned &&
      (Dsection4.followers.map(p => p._1).toSet intersect Esection1.followers.map(p => p._1).toSet) == Set(playerA))

    assert(Dsection4.openEdges == 0 && Esection1.openEdges == 0)
  }
  it should "return the number of points for each player when city section is closed" in new SimpleBoard with GameCollection{
    board.setMove(move1)
    E.orientation = Right
    board.setMove(move2)

    assert(playerA.points == 4 && playerB.points == 0)
  }

  it should "get available moves" in new MiniGame {
    val moves1 = board.getMoves(J, playerA)
    assert(moves1.size == 30)

    val moves2 = board.getMoves(L, playerA).size
    assert(moves2 == 49)

    val moves3 = board.getMoves(B, playerA).size
    assert(moves3 == 12)
  }
  it should "know how to union cities" in new MiniGame {
    val move1 = new Move(L, (0, -1), Some(Lsection6), playerA)
    Q.orientation = Right
    val move2 = new Move(Q, (1, 0), Some(Qsect1), playerB)
    J.orientation = Down
    val move3 = new Move(J, (1, 1), None, playerA)
    val move4 = new Move(O, (1, -1), None, playerB)

    board.setMove(move1)
    assert(Lsection6.followers.contains(playerA))

    board.setMove(move2)
    assert(Qsect1.followers.contains(playerB))

    board.setMove(move3)

    board.setMove(move4)
    assert(Qsect1.followers.contains(playerA) && Qsect1.followers.contains(playerB))

    assert(Osect1.openEdges == 0)
    assert(playerB.points == 10 && playerA.points == 10)
  }
  it should "evaluate the board points in case of 'game end' event"
}
