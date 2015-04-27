package com.game

import com.tile.{Section, Tile}

trait TileBag {

  val startTile : Tile
  def getTile : Option[Tile]
  def getEntireTileBag : Iterable[Tile]
  def finished : Boolean
  def allSections : Set[Section]
}
