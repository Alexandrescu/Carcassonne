package com.tile

import com.game.Player

class MonasterySection(override val frontEndId : Int) extends Section(frontEndId) {
  private var tileCount : Int = 0

  def addOpen() : Unit = {
    tileCount += 1
  }

  def removeOpen() : Unit = {
    tileCount -= 1
  }

  override def isOwned: Boolean = owners.nonEmpty

  override def addOwners(newOwners: Map[Player, Int]): Unit = {
    if(newOwners.size > 1)
      throw new Exception("Can't have more than one owner on Monastery")
    _owners = newOwners
  }

  override def owners: Map[Player, Int] = _owners

  override def updateClose(): Unit = {
    if(tileCount == 0) {
      var flagIterated = false
      owners.foreach{case (player, followers) =>
        if(flagIterated) throw new Error("Monastery should only have one owner")

        flagIterated = true
        player.addPoints(9)
      }
    }
  }
}
