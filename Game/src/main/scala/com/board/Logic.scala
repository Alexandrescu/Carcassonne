package com.board

import Direction
import com.tile._
import Direction.Direction

trait Logic {
  def isMove(tile : Tile, tiles : Map[Direction, Tile]) : Option[Map[Section, Set[Section]]]
}
