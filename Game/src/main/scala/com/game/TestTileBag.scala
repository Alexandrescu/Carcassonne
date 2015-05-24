package com.game

import com.tile.{Section, Tile}

import scala.collection.mutable.ArrayBuffer

class TestTileBag extends TileBag{
  override val startTile: Tile = TileGenerator.D

  override def allSections: Set[Section] = {
    result
  }

  override def getEntireTileBag: Set[Tile] = ???

  override def hasNext: Boolean = bag.size > 0

  val bag = ArrayBuffer[Tile]()
  bag += TileGenerator.D
  bag += TileGenerator.C

  var result : Set[Section] = Set()
  for(tile <- bag) {
    result = result union tile.getSections()
  }

  private var thisTile : Tile = startTile
  override def next(): Tile = {
    thisTile = bag.remove(0)
    thisTile
  }

  override def current: Tile = thisTile

  override def getTile(name: String): Tile = ???

  override def remove(tile: Tile): Unit = ???
}
