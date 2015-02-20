package com.game

import main.scala.Tile

trait Board {
  def get(i : Int, j : Int) : Tile
  def getBoard() : Set[Tile]
  def isPlaceMove(tile : Tile, follower : Follower, i : Int, j : Int) : Boolean
  def placeMove(tile : Tile, follower : Follower, i : Int, j : Int) : Boolean
}
