package com.tile

import com.game.Player

abstract class Section{
  var treeDepth : Int = 1

  protected var _owners : Set[Player] = Set()
  def isOwned : Boolean
  def addOwners(newOwners : Set[Player]): Unit
  def owners : Set[Player]
}

class GrassSection extends Section{
  var parent : Option[GrassSection] = None
  var closedCities : Set[CitySection] = Set()

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

  override def addOwners(newOwners: Set[Player]): Unit = {
    val root = findRoot()
    root._owners ++= newOwners
  }

  override def owners: Set[Player] = findRoot()._owners
}


class CitySection extends Section{
  private var _openEdges : Int = 0
  private var _tileCount : Int = 1
  var parent : Option[CitySection] = None
  private var adjacentGrass : Set[GrassSection] = Set()

  def findRoot() : CitySection = {
    var root = this
    while(root.parent.isDefined) {
      root = root.parent.get
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
    if(parent.isEmpty) return owners.nonEmpty
    findRoot().isOwned
  }

  override def addOwners(newOwners: Set[Player]): Unit = {
    val root = findRoot()
    root._owners ++= newOwners
  }
  override def owners: Set[Player] = findRoot()._owners
}


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

  override def addOwners(newOwners: Set[Player]): Unit = {
    val root = findRoot()
    root._owners ++= newOwners
  }
  override def owners: Set[Player] = findRoot()._owners
}


class MonasterySection extends Section {
  private var tileCount : Int = 0

  def addOpen() : Unit = {
    tileCount += 1
  }

  def removeOpen() : Unit = {
    tileCount -= 1
  }

  override def isOwned: Boolean = owners.nonEmpty

  override def addOwners(newOwners: Set[Player]): Unit = {
    if(newOwners.size > 1)
      throw new Exception("Can't have more than one owner on Monastery")
    _owners = newOwners
  }

  override def owners: Set[Player] = _owners
}