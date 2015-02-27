package com.game

import main.scala.Direction.Direction
import main.scala.Tile

import scala.collection.mutable.{HashSet, HashMap}

trait Logic {
  def isMove(tile : Tile, tiles : Map[Direction, Tile]) : Option[HashMap[TileSection, HashSet[BoardSection]]]
}
