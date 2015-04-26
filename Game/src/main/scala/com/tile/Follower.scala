package com.tile

import com.game.Player

// Player is who's follower this is.
class Follower(val player : Player) {
  private var _section : Option[Section] = None

  def section() : Option[Section] = _section
  def section_= (newSection : Section) : Unit = {
    _section = Some(newSection)
  }

  def isPlaced : Boolean = _section.isDefined

  def take() = {
    _section = None
  }
}
