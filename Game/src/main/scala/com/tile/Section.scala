package com.tile

class Section{
  var isPlayed : Boolean = false
  def isOwned = ???
}

class GrassSection extends Section{
  var parent : Option[GrassSection] = None
  var closedCities : Int = 0
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