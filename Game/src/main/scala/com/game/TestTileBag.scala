package com.game

import com.tile.{Section, Tile}

import scala.collection.mutable.ArrayBuffer

class TestTileBag extends TileBag{
  override val startTile: Tile = TileGenerator.D

  override def allSections: Set[Section] = ???

  override def getEntireTileBag: Iterable[Tile] = ???

  override def hasNext: Boolean = false

  val bag = ArrayBuffer[Tile]()

  bag += TileGenerator.A
  bag += TileGenerator.B

  override def getTile: Option[Tile] = {
    Some(bag.remove(0))
  }
}
