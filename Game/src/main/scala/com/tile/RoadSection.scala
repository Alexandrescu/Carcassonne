package com.tile

import com.game.Player

class RoadSection(override val frontEndId : Int) extends Section(frontEndId){
  private var _openEdges : Int = 0
  private var _tileCount : Int = 1
  private var _parent : Option[RoadSection] = None

  def parent(roadSection: RoadSection): Unit = {
    val parentRoot = roadSection.findRoot()
    val root = this.findRoot()

    root._parent = Some(parentRoot)
  }

  def openEdges : Int = _openEdges

  def findRoot() : RoadSection = {
    var root = this
    while(root._parent.isDefined) {
      root = root._parent.get
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
    if(_parent.isEmpty) return owners.nonEmpty
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
