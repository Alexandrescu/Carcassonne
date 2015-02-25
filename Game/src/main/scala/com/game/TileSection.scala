package com.game

abstract class TileSection {
  def isOwned : Boolean
}
case class Follower(id : Integer) extends TileSection {
  override def isOwned: Boolean = true
}
case class Section(id : Integer) extends TileSection {
  override def isOwned: Boolean = false
}