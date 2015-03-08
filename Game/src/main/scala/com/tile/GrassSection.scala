package com.tile

import com.game.Player

class GrassSection extends Section{
  var parent : Option[GrassSection] = None
  private var _closedCities : Set[CitySection] = Set()

  def addClosedCities(cities : Set[CitySection]): Unit = {
    findRoot()._closedCities ++= cities
  }

  def closedCities() : Set[CitySection] = _closedCities

  def findRoot() : GrassSection = {
    var root = this
    while(root.parent.isDefined) {
      root = root.parent.get
    }
    root
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

  override def updateClose(): Unit = {}
}
