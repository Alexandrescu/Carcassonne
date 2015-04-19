package com.tile

import com.game.Player

class CitySection(override val frontEndId : Int) extends Section(frontEndId){
  private var _openEdges : Int = 0
  private var _tileCount : Int = 1
  private var _parent : Option[CitySection] = None
  private var adjacentGrass : Set[GrassSection] = Set()

  def parent(citySection: CitySection): Unit = {
    val parentRoot = citySection.findRoot()
    val root = this.findRoot()

    root._parent = Some(parentRoot)
  }

  def findRoot() : CitySection = {
    var root = this
    while(root._parent.isDefined) {
      root = root._parent.get
    }
    root
  }

  def getGrass() : Set[GrassSection] = {
    val root = findRoot()
    val grass = root.adjacentGrass

    root.adjacentGrass = grass.map(g => g.findRoot())
    root.adjacentGrass
  }

  def addGrass(grassSet : Set[GrassSection]) = {
    val root = findRoot()
    root.adjacentGrass = root.getGrass() ++ grassSet
  }

  def tileCount() : Int = _tileCount

  def addTiles(x : Int) : Unit = {
    findRoot()._tileCount += x
  }

  def openEdges : Int = _openEdges

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
      root.getGrass().foreach(grass => grass.addClosedCities(Set(root)))

      var maxFollowers = 0
      root._owners.foreach{case (player, followers) => maxFollowers = Math.max(followers, maxFollowers)}
      root._owners.filter(p => p._2 == maxFollowers).foreach{case (player, _) => player.addPoints(root.tileCount() * 2)}
      root._owners.foreach{case (player, followers) => player.addFollowers(followers)}
    }
  }
}
