package com.board

import com.game.Direction
import com.tile._
import Direction.Direction

trait Logic {
  def isMove(tile : Tile, tiles : Map[Direction, Tile]) : Option[Map[Section, Set[Section]]]
}
