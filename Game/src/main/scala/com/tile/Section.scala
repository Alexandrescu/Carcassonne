package com.tile

import com.game.Player

class Section{
  var treeDepth : Int = 1
  var isPlayed : Boolean = false
  def isOwned = !_owners.isEmpty
  private var _owners : Set[Player] = Set()
  def addOwners(newOwners : Set[Player]): Unit = {
    _owners = _owners ++ newOwners
  }
  def owners : Set[Player] = _owners
}

class GrassSection extends Section{
  var parent : Option[GrassSection] = None
  var closedCities : Set[CitySection] = Set()

  def findRoot() : GrassSection = {
    var root = this
    while(!root.parent.isEmpty) {
      root = root.parent.get
    }
    root
  }
}

class CitySection extends Section{
  var openEdges : Int = 0
  var tileCount : Int = 0
  var parent : Option[CitySection] = None
  var adjacentGrass : Set[GrassSection] = Set()
}

class RoadSection extends Section{
  var openEdges : Int = 0
  var tileCount : Int = 0
  var parent : Option[RoadSection] = None
}
class MonasterySection extends Section {
  var tileCount : Int = 0
}