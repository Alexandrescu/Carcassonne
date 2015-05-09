package com.game

import com.tile.{Section, Tile}

trait TileBag {

  val startTile : Tile
  def next() : Tile
  def current : Tile
  def hasNext : Boolean
  def getEntireTileBag : Iterable[Tile]
  def allSections : Set[Section]
}
