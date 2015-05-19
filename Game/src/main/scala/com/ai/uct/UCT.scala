package com.ai.uct

import com.board.{GameBoard, Move, RemovedFollower}
import com.game.StandardTileBag
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class UCT(moveList: ArrayBuffer[Either[Move, RemovedFollower]], playerNumber : Int, knownTile : Option[Tile], mySlot : Int) {
  /*
    We create a perfect copy of the board and the moves from the moveList.
    This will update our player model and tile bag accordingly.
    Note: a tile bag is a collection of all the tiles in the basic game of Carcassonne.
  */

  private val board : GameBoard = new GameBoard(Factory.logic, Factory.keeper)
  private val tiles : StandardTileBag = new StandardTileBag
  private val playerTurn: PlayerTurn = new PlayerTurn(playerNumber)

  /* Create map of players */

  for(m <- moveList) m match {
    case Left(move) =>
      val myMove = adoptMove(move)

      board.setMove(myMove)
      tiles.remove(myMove.tile)
    case Right(follower) =>
  }
  /* From here onwards we should have a perfect copy of the board */

  /*
    The uctConstant varies the selectivity of the search:
    Small values will be very selective, while large ones will give uniform search.

    The uctVisits is enforcing a minimum number of exploration of a node before we expand.
  */
  private val random = new Random()
  private val uctConstant :Double = 4
  private val uctVisits : Int = 10
  private val oo = 10000 /* infinity */

  /* Selection phase */
  def UCTSelect(node : Node) : Option[Node] = {
    var bestUCT = 0.0
    var result : Option[Node] = None

    for(child <- node.children) {
      val uctValue = if(child.visits > 0) {
        /* UCT Formula; the 2 in the sqrt is supressed in the uctConstant */
        uctConstant * (Math.sqrt(Math.log(node.visits)/ child.visits) + child.winRate)
      }
      else {
        /* childNode not visited, so the fraction would have yield infinity */
        oo + random.nextInt(1000)
      }

      /* Updating the best node to select based on UCT */
      if(uctValue > bestUCT) {
        bestUCT = uctValue
        result = Some(child)
      }
    }
    result
  }

  /*
      Simulation phase
      Doing a random playout and returning the value of the simulation, X, in the interval [0, 1].
  */
  def playRandomGame()  : Unit = {
    while(tiles.hasNext) {
      val nextPlayer = playerTurn.next()
      var nextTile = tiles.next()

      var moves = board.getMoves(nextTile, nextPlayer).toVector
      /* In case we can't play the tile, we discard the tile */
      while(moves.size == 0) {
        nextTile = tiles.next()
        moves = board.getMoves(nextTile, nextPlayer).toVector
      }

      val movePicked = moves(random.nextInt(moves.size))
      val sectionPicked = movePicked.toOwnFromTile(random.nextInt(movePicked.toOwnFromTile.size))
      nextTile.orientation = movePicked.direction
      board.setMove(new Move(nextTile, movePicked.place, sectionPicked, nextPlayer))
    }

    /* This iteration is doing the end evaluation of the board. */
    for(section <- tiles.allSections) {
      section.closeAtEnd()
    }
  }

  /* Exploration + Backpropagation phase */
  def playSimulation(node : Node, nodeOwner : Int): Unit = {

    if (!node.hasChildren && node.visits < uctVisits) {
      // Run a number of simulations before children are expanded
      playRandomGame()
    }
    else {
      /*
        We either haven't reached a terminal node (leaf) or
        we have explored this sequence of moves enough time.
      */
      if (!node.hasChildren) {
        expand(node)
      }

      val nextNode = UCTSelect(node) match {
        case Some(n) => n
        case None =>
          // No moves left to explore in the tree
          return
      }

      if (nextNode.move.isEmpty) {
        // Should always have a move set.
        throw UninitializedFieldError("Move has not been set.")
      }

      /* Translating the move to this current board */
      val move = adoptMove(nextNode.move.get)

      board.setMove(move)
      tiles.remove(move.tile)
      playSimulation(nextNode, move.player.slot)
    }

    /* Propagating the simulation result in as is, and "complementing" the value when is not my turn. */
    node.update(playerTurn.winResult(nodeOwner))
  }

  private def adoptMove(move : Move) : Move = {
    val tile = tiles.getTile(move.tile.identifier)
    tile.orientation = move.tile.orientation
    val section = move.toOwnFromTile match {
      case Some(s) => tile.getSectionById(s.frontEndId)
      case None => None
    }

    val player = if(move.player != null) {
      playerTurn.setCurrent(move.player.slot)
      playerTurn.getBySlot(move.player.slot)
    }
    else {
      playerTurn.setCurrent(-1)
      null
    }
    new Move(tile, move.place, section, player)
  }

  /*
      We are considering 2 types of budget:

      1. iteration number in uctSearch
      2. number of seconds in uctSearchSeconds

      The state of the root node is encapsuled by initialisation of the object
  */
  def uctSearch(simulations : Int) : Move = {
    val root = new Node
    expand(root)

    for(simNo <- 1 to simulations) {
      val clone = new UCT(moveList, playerNumber, None, mySlot)
      clone.playSimulation(root, mySlot)
    }

    val bestNode = root.getBest
    bestNode.move.get
  }

  def uctSearchSeconds(seconds : Int) : Move = {
    val root = new Node
    expand(root)

    val start = java.lang.System.currentTimeMillis()
    val end = start + seconds * 1000 // 1000 ms/sec
    while(java.lang.System.currentTimeMillis() < end) {
      val clone = new UCT(moveList, playerNumber, None, mySlot)
      clone.playSimulation(root, mySlot)
    }

    val bestNode = root.getBest
    bestNode.move.get
  }

  /*
      When expanding the node, we either know the drew tile, or we just add a move for every tile type left.
  */
  def expand(node : Node): Unit = {
    /* Deciding the set of tiles for the actions */
    val expandingTiles : Set[Tile] = knownTile match {
      case Some(tile) =>
        /*
          Tiles have mutable state, so they are not shared by boards.
          getTile retreives the currents board copy of that tile.
        */
        playerTurn.setCurrent(mySlot - 1)
        Set(tiles.getTile(tile.identifier))
      case None =>
        tiles.getEntireTileBag
    }

    /* In case we are expanding a possible tile we keep track of them */
    var expanded : Set[String] = Set()
    val player = playerTurn.next()
    var children = new ArrayBuffer[Node]()

    for(tile <- expandingTiles) {
      if(!expanded(tile.identifier)) {
        expanded += tile.identifier
        /* This is the board at the current state */
        val validMoves = board.getMoves(tile, player)

        for (move <- validMoves) {
          for (section <- move.toOwnFromTile) {
            /* Creating the move to store */
            tile.orientation = move.direction
            val newNode = new Node
            newNode.move = new Move(tile, move.place, section, player)

            /* Evaluating the immediate reward of this move */
            val (pDelta, fDelta) = Factory.moveValuation(moveList, newNode.move.get, playerNumber, mySlot, player.slot)
            newNode.stats(pDelta, fDelta)

            children += newNode
          }
        }
      }
    }

    node.children = children
  }
}
