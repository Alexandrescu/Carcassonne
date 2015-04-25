package com.game

import com.board.{PossibleMove, Move, GameBoard}
import com.tile.Tile

class Game(board : GameBoard, tileBag : TileBag, callEndOfGame : () => Unit ) {

  def drawTile() : Tile = {
    val tile = tileBag.getTile.get
    if(tileBag finished) {
      callEndOfGame()
    }
    tile
  }

  def isMove(move : Move): Boolean = {
    board.isMove(move)
  }

  def setMove(move : Move): Unit = {
    board.setMove(move)
  }

  def getMoves(tile : Tile, player : Player) : Set[PossibleMove] = {
    board.getMoves(tile, player)
  }

  // Execute first move and it
  def setBoard() : Move = {
    val move = new Move(tileBag.startTile, (0, 0), None, null)
    board.setMove(move)
    move
  }

  def endGame() = ???
}
