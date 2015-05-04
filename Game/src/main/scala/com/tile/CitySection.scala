package com.tile

import com.player.Follower

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
    if(_parent.isEmpty) return followers.nonEmpty
    findRoot().isOwned
  }

  override def addFollowers(newFollowers: Set[Follower]): Unit = {
    val root = findRoot()
    root._followers = root._followers.union(newFollowers)
  }

  override def followers: Set[Follower] = findRoot()._followers

  override protected def canClose(): Boolean = openEdges == 0

  override protected def closeInGame(): Unit = closeWithPoints(tileCount() * 2)

  override protected def closeAtEnd(): Unit = closeWithPoints(tileCount())
}
