package main.scala

import sun.reflect.generics.reflectiveObjects.NotImplementedException

import scala.collection.immutable.HashMap

/**
 * Created by Andrei on 25/01/15.
 */

class State(private val tileBag : TileBag) {
  def getPossibleMoves(tile: Tile) : Iterable[(Integer, Integer)]= ???

  private var board : Map[(Integer, Integer), Tile] = new HashMap[(Integer, Integer), Tile]
  board = board + (((0, 0), tileBag.startTile))

  def getBoard() = board

  def playMove(): Unit = {
    throw new NotImplementedException()
  }


}
