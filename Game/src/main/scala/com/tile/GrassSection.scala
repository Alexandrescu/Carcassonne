package com.tile

import com.game.Player

class GrassSection extends Section{
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

  override def updateClose(): Unit = {}
}
