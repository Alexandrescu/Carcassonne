package com.tile

import com.game.Player

abstract class Section(val frontEndId : Int) {
  var treeDepth : Int = 1

  protected var _owners : Map[Player, Int] = Map()
  def isOwned : Boolean
  def addOwners(newOwners : Map[Player, Int]): Unit
  def owners : Map[Player, Int]

  def updateClose()
}










