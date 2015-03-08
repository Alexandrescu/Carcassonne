package com.tile

import com.game.Player

class RoadSection extends Section{
  private var _openEdges : Int = 0
  private var _tileCount : Int = 1
  var parent : Option[RoadSection] = None

  def openEdges : Int = _openEdges

  def findRoot() : RoadSection = {
    var root = this
    while(root.parent.isDefined) {
      root = root.parent.get
    }
    root
  }

  def tileCount() : Int = _tileCount

  def addTiles(x : Int) : Unit = {
    findRoot()._tileCount += x
  }

  def addOpen(x : Int = 1) : Unit = {
    findRoot()._openEdges += x
  }

  def removeOpen(x : Int = 1) : Unit = {
    findRoot()._openEdges -= x
  }

  override def isOwned: Boolean = {
    if(parent.isEmpty) return owners.nonEmpty
    findRoot().isOwned
  }

  override def addOwners(newOwners: Map[Player, Int]): Unit = {
    val root = findRoot()

    newOwners.foreach{ case (player, followers) =>
      val newFollowers = root._owners.getOrElse(player, 0) + followers
      if(newFollowers != 0) {
        root._owners -= player
        root._owners += (player -> newFollowers)
      }
    }
  }
  override def owners: Map[Player, Int] = findRoot()._owners

  override def updateClose(): Unit = {
    val root = findRoot()
    if(root.openEdges == 0) {
      //closed
      var maxFollowers = 0
      root._owners.foreach{case (player, followers) => maxFollowers = Math.max(followers, maxFollowers)}
      root._owners.filter(p => p._2 == maxFollowers).foreach{case (player, _) => player.addPoints(root.tileCount())}
      root._owners.foreach{case (player, followers) => player.addFollowers(followers)}
    }
  }
}
