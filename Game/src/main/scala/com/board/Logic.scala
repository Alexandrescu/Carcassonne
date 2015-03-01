package com.board

import com.tile._
import main.scala.Direction.Direction

trait Logic {
  def isMove(tile : Tile, tiles : Map[Direction, Tile]) : Option[Map[Section, Set[Section]]]
}
