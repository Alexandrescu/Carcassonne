package com.game

import main.scala.Player

trait UnionKeeper {
  def add(section: TileSection) : BoardSection

  def isOwned(boardSection : Int) : Boolean
  def isOwned(boardSection : BoardSection) : Boolean

  def union(boardSectionA: BoardSection, boardSectionB: BoardSection) : BoardSection
  def own(boardSection: BoardSection, player : Player) : Unit

  def base : BoardSection
}
