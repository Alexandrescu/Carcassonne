package com.board

import org.scalatest.FlatSpec

class BoardTest extends FlatSpec {
  trait SimpleBoard {
    val board : Board = new GameBoard(new StandardLogic(), null)
  }

  "Board" should "contain a collection of the tiles creating the map"
  it should "return a valid tile when querying a valid position on the board"
  it should "know if the move is valid"
  it should "place a move correctly - update the ownership"
  it should "return the number of points for each player when section is closed"
  it should "evaluate the board points in case of 'game end' event"
}
