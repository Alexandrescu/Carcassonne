package com.game

import com.tile.{Section, Tile}

trait TileBag {
  def remove(tile : Tile)
  def getTile(name : String) : Tile
  val startTile : Tile
  def next() : Tile
  def current : Tile
  def hasNext : Boolean
  def getEntireTileBag : Set[Tile]
  def allSections : Set[Section]
}
