package com.game

case class Section(id : Int) {
  var isPlayed : Boolean = false
}

case class GrassSection(override val id : Int) extends Section(id) {
  var parent : Option[GrassSection] = None
  var closedCities : Int = 0
}

case class CitySection(override val id : Int) extends Section(id) {
  var openEdges : Int = 0
  var tileCount : Int = 0
  var parent : Option[CitySection] = None
  var adjacentGrass : Set[GrassSection] = Set()
}

case class RoadSection(override val id : Int) extends Section(id) {
  var openEdges : Int = 0
  var tileCount : Int = 0
  var parent : Option[RoadSection] = None
}
case class MonasterySection(override val id : Int) extends Section(id) {
  var tileCount : Int = 0
}