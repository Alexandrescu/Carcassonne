package com.board

import com.board.Direction.Direction
import com.tile._

trait Logic {
  def isMove(tile : Tile, tiles : Map[Direction, Tile]) : Option[Map[Section, Set[Section]]]
}
