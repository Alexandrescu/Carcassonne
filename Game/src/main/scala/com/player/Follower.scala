package com.player

import com.tile.Section

// Player is who's follower this is.
class Follower(val player : Player) {
  /*
    When removing a follower I need to
      know the coordinates and the section id.
   */
  final type Place = (Int, Int)

  private var _section : Option[Section] = None

  def section() : Option[Section] = _section
  def section_= (newSection : Section) : Unit = {
    _section = Some(newSection)
  }

  private var _place : Option[Place] = None

  def place() : Option[Place] = _place
  def place_= (newPlace : Place) : Unit = {
    _place = Some(newPlace)
  }

  def isPlaced : Boolean = _section.isDefined

  private var _removedPlace : Place = (0, 0)
  private var _removedFrontEnd : Int = 0

  def removedPlace : Place = _removedPlace
  def removedFrontEndId = _removedFrontEnd

  def take() = {
    _removedFrontEnd = _section.get.frontEndId
    _removedPlace = _place.get

    player.followerRemoved(this)

    _section = None
  }
}
