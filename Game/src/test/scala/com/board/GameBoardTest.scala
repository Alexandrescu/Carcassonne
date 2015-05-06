package com.board

import com.game.TileGenerator
import com.player.{Follower, PlayerObserver, Player}
import com.tile.{Section, MonasterySection}
import org.scalatest.{PrivateMethodTester, BeforeAndAfter, FlatSpec}

class GameBoardTest extends FlatSpec with BeforeAndAfter with PrivateMethodTester{
  final type Place = (Int, Int)

  val board = new GameBoard(new StandardLogic, new SectionKeeper)
  val startTilePlace = (0,0)

  val bOutline = PrivateMethod[Map[Place, Set[Section]]]('boardOutline)
  val tOutline = PrivateMethod[Set[Place]]('tileOutline)
  val sectionClosed = PrivateMethod[Boolean]('closed)

  val observer = new PlayerObserver {
    var follower : Option[Follower] = None
    override def followerUpdate(follower: Follower): Unit = {
      this.follower = Some(follower)
    }
  }

  val player = new Player(0, Set(observer))
  val playerWithNoFollowers = new Player(1) {
    override def hasFollower : Boolean = false
  }

  before {
    board.setMove(new Move(TileGenerator.D, startTilePlace, None, null))
  }

  "Game board" should "get" in {
    assert(board.get(startTilePlace).isDefined)
    assert(board.get(startTilePlace).get.identifier == "D")
    assert(board.get(0, 1).isEmpty)
  }

  it should "have the board outline for the starting tile" in {
    val tileOutline = board invokePrivate tOutline()
    assert(tileOutline.size == 4)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 3)

    val places = Set[(Place, Int)] (
      ((0, 1), 1),
      ((0,-1), 1),
      ((1, 0), 1)
    )

    for((place, size) <- places) {
      assert(boardOutline.get(place).isDefined)
      assert(boardOutline.get(place).size == size)
    }
  }

  it should "get moves when player has followers" in {
    val L = TileGenerator.L
    val moves = board.getMoves(L, player)
    assert(moves.toSeq.map(_.toOwnFromTile.size).sum == 56)

    assert( moves.forall(m => m.toOwnFromTile.forall
      (s => s.isEmpty || (L.getSections() contains s.get))
    ))
  }

  it should "get moves when player has no followers" in {
    val L = TileGenerator.L
    val moves = board.getMoves(L, playerWithNoFollowers)

    assert(moves.toSeq.map(_.toOwnFromTile.size).sum == 7)
    assert(moves.forall(m => m.toOwnFromTile.forall(s => s.isEmpty)))
  }

  it should "get the board" in {
    assert(board.getBoard().size == 1)
    assert(board.getBoard().forall(p => p._1 == (0, 0) && p._2.identifier == "D"))
  }

  it should "detect invalid move" in {
    val L = TileGenerator.L
    val move = new Move(L, (1, 0), L.getSectionById(7), player)

    assert(!board.isMove(move))
    intercept[Error] {
      board.setMove(move)
    }
  }

  it should "set a valid move" in {
    val L = TileGenerator.L
    L.orientation = Direction.Down
    val move = new Move(L, (1, 0), L.getSectionById(7), player)

    assert(board.isMove(move))
    board.setMove(move)

    assert(board.getBoard().size == 2)
    assert(player.points == 4 && player.followers == 7)
    assert(L.getSectionById(7).isDefined && L.getSectionById(7).get.closed)

    L.getSectionById(7).get.finishSection()
    L.getSectionById(7).get.closeSection()

    assert(player.points == 4 && player.followers == 7)
    assert(observer.follower.isDefined)
  }

  // Tile L has been placed

  it should "have update the board and tile outline accordingly" in {
    val tileOutline = board invokePrivate tOutline()
    assert(tileOutline.size == 6)

    val boardOutline = board invokePrivate bOutline()
    assert(boardOutline.size == 5)

    val places = Set[(Place, Int)] (
      ((0, 1), 1),
      ((0,-1), 1),
      ((1, 1), 1),
      ((1,-1), 1),
      ((2, 0), 1)
    )

    for((place, size) <- places) {
      assert(boardOutline.get(place).isDefined)
      assert(boardOutline.get(place).size == size)
    }
  }

  it should "return 0 possible moves when no moves available" in {
    val C = TileGenerator.C
    assert(board.getMoves(C, player).size == 0)
  }

  it should "know how to take ownership of monasteries" in {
    val A = TileGenerator.A
    assert(A.getSectionById(2).isDefined)
    assert(A.getSectionById(2).get.isInstanceOf[MonasterySection])

    val move = new Move(A, (1, 1), A.getSectionById(2), player)
    assert(board.isMove(move))

    board.setMove(move)
    assert(board.getBoard().size == 3)
    assert(!A.getSectionById(2).get.closed)

    val boardOutline = board invokePrivate bOutline()
    val tileOutline = board invokePrivate tOutline()

    assert(boardOutline.size == 8)
    assert(tileOutline.size == 7)

    assert(player.points == 4 && player.followers == 6)
    A.getSectionById(2).get.finishSection()
    assert(player.points == 7 && player.followers == 7)
  }
}
