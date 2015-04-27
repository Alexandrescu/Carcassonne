package com.tile

import com.player.Follower

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
    if(_parent.isEmpty) return followers.nonEmpty
    findRoot().isOwned
  }

  override def addFollowers(newFollowers: Set[Follower]): Unit = {
    val root = findRoot()
    root._followers = root._followers.union(newFollowers)
  }
  override def followers: Set[Follower] = findRoot()._followers

  override def closeSection(): Unit = {
    val root = findRoot()
    if(root.openEdges == 0) {
      //closed
      for(player <- majority(root._followers)) {
        player.addPoints(root.tileCount())
      }
      removeFollowers(root._followers)
    }
  }
}
