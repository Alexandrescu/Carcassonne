package com.tile

class GrassSection(override val frontEndId : Int) extends Section(frontEndId){
  private var _parent : Option[GrassSection] = None
  private var _closedCities : Set[CitySection] = Set()

  def parent(grassSection: GrassSection): Unit = {
    val parentRoot = grassSection.findRoot()
    val root = this.findRoot()

    root._parent = Some(parentRoot)
  }

  def addClosedCities(cities : Set[CitySection]): Unit = {
    findRoot()._closedCities ++= cities
  }

  def closedCities() : Set[CitySection] = _closedCities

  def findRoot() : GrassSection = {
    var root = this
    while(root._parent.isDefined) {
      root = root._parent.get
    }
    root
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
    for(player <- majority(root._followers)) {
      player.addPoints(root.pointCount)
    }
  }

  def pointCount : Int = {
    _closedCities.size * 3
  }
}
