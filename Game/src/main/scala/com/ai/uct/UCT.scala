package com.ai.uct

import com.board.{GameBoard, Move, RemovedFollower}
import com.game.StandardTileBag
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class UCT(moveList: ArrayBuffer[Either[Move, RemovedFollower]], playerNumber : Int, knownTile : Option[Tile], mySlot : Int) {

  /*
      We have a perfect copy of the board with a new set of tiles created from the moveList.
      We are going to run the simulation on this.
   */

  val board : GameBoard = new GameBoard(Factory.logic, Factory.keeper)
  val tiles : StandardTileBag = new StandardTileBag
  val playerTurn: PlayerTurn = new PlayerTurn(playerNumber)

  for(m <- moveList) m match {
    case Left(move) =>
      val myMove = adoptMove(move)

      board.setMove(myMove)
      tiles.remove(myMove.tile)
    case Right(follower) =>
  }

  println("Generating board complete.")
  /* From here on we should have a perfect copy of the board */

  /*
    This varies the selectivity of the search:
    Small values will be very selective, while large ones will give uniform search
  */
  val random = new Random()
  val uctConstant = 1
  val uctVisits = 10
  
  def UCTSelect(node : Node) : Option[Node] = {
    var bestUCT = 0.0
    var result : Option[Node] = None

    var nextNode = node.child

    while(nextNode nonEmpty) {
      val next = nextNode.get

      val uctValue = if(next.visits > 0) {
        val uct = uctConstant * Math.sqrt(Math.log(node.visits)/ next.visits)
        next.winRate + uct
      }
      else {
        // Play the random unexplored move
        10000 + random.nextInt(1000)
      }

      if(uctValue > bestUCT) {
        bestUCT = uctValue
        result = nextNode
      }

      nextNode = next.sibling
    }
    result
  }

  def playRandomGame()  : Double = {
    while(tiles.hasNext) {
      val nextPlayer = playerTurn.next()

      var nextTile = tiles.next()
      var moves = board.getMoves(nextTile, nextPlayer).toVector
      while(moves.size == 0) {
        nextTile = tiles.next()
        moves = board.getMoves(nextTile, nextPlayer).toVector
      }

      val movePicked = moves(random.nextInt(moves.size))
      val sectionPicked = movePicked.toOwnFromTile(random.nextInt(movePicked.toOwnFromTile.size))
      nextTile.orientation = movePicked.direction
      board.setMove(new Move(nextTile, movePicked.place, sectionPicked, nextPlayer))
    }

    for(section <- tiles.allSections) {
      section.closeAtEnd()
    }
    playerTurn.winResult(mySlot)
  }

  def playSimulation(node : Node): Double = {
    val randomResult =
      if (node.child.isEmpty && node.visits < uctVisits) {
        // Run a number of simulations before children are expanded
        playRandomGame()
      }
      else {
        if (node.child.isEmpty) {
          expand(node)
        }

        val nextNode = UCTSelect(node).get

        if (nextNode.move.isEmpty) {
          throw UninitializedFieldError("Move has not been set.")
        }

        val move = adoptMove(nextNode.move.get)

        board.setMove(move)
        tiles.remove(move.tile)
        /* We might fiddle with this when more players are added*/
        1 - playSimulation(nextNode)
      }

    node.update(1 - randomResult)
    randomResult
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

  def uctSearch(simulations : Int) : Move = {
    val root = new Node
    expand(root)

    for(simNo <- 1 to simulations) {
      val clone = new UCT(moveList, playerNumber, None, mySlot)
      clone.playSimulation(root)
    }

    val bestNode = root.getBest
    bestNode.move.get
  }

  /*
      When we are expanding the node, we either know are draw, or we just add a move for every tile type left.
      Expanding also takes us to the next player.
   */
  def expand(node : Node): Unit = {
    val expandingTiles : Set[Tile] = knownTile match {
      case Some(tile) =>
        Set(tiles.getTile(tile.identifier))
      case None =>
        tiles.getEntireTileBag
    }

    var expanded : Set[String] = Set()
    val player = playerTurn.next()
    for(tile <- expandingTiles) {
      if(!expanded(tile.identifier)) {
        expanded += tile.identifier
        /* This is the board at the current state */
        val validMoves = board.getMoves(tile, player)

        var lastSeenNode = node

        for (move <- validMoves) {
          for (section <- move.toOwnFromTile) {
            /* Creating the move to store */
            tile.orientation = move.direction
            val newNode = new Node
            newNode.move = new Move(tile, move.place, section, player)

            /* Now append the new Node in the right place */
            if (node == lastSeenNode) {
              /* Adding child */
              lastSeenNode.child = Some(newNode)
            }
            else {
              /* Completing the layer, i.e. siblings */
              lastSeenNode.sibling = Some(newNode)
            }
            /* Moving to the next node */
            lastSeenNode = newNode
          }
        }
      }
    }
  }
}
