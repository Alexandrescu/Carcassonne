package com.game

import main.scala.Tile

class GameBoard extends Board{
  override def get(i: Int, j: Int): Tile = ???

  override def placeMove(tile: Tile, follower: Follower, i: Int, j: Int): Boolean = ???

  override def getBoard(): Set[Tile] = ???

  override def isPlaceMove(tile: Tile, follower: Follower, i: Int, j: Int): Boolean = ???
}
