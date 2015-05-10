package com.ai.uct

import com.board._
import com.game.StandardTileBag

import scala.collection.mutable.ArrayBuffer

object Factory {
  val keeper = new SectionKeeper
  val logic = new StandardLogic

  /* Returns the point delta and the follower delta */
  def moveValuation(moveList: ArrayBuffer[Either[Move, RemovedFollower]], newMove : Move, playerNumber : Int, mySlot : Int, currentPlayer : Int): (Int, Int) = {
    if(currentPlayer != mySlot) {
      return (0, 0)
    }
    val board : GameBoard = new GameBoard(Factory.logic, Factory.keeper)
    val tiles : StandardTileBag = new StandardTileBag
    val playerTurn: PlayerTurn = new PlayerTurn(playerNumber)

    def adoptMove(move : Move) : Move = {
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

    for(m <- moveList) m match {
      case Left(move) =>
        val myMove = adoptMove(move)

        board.setMove(myMove)
        tiles.remove(myMove.tile)
      case Right(follower) =>
    }

    val adoptedMove = adoptMove(newMove)
    val me =  playerTurn.getBySlot(mySlot)
    val startPoints = me.points + me.latentPoints
    val startFollowers = me.followers

    board.setMove(adoptedMove)

    val endFollowers = me.followers

    for(section <- tiles.allSections) {
      section.closeAtEnd()
    }

    val endPoints = me.points

    (endPoints - startPoints, endFollowers - startFollowers)
  }
}
