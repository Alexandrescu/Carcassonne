package com.board

import com.tile.{MonasterySection, RoadSection, CitySection, GrassSection}
import main.scala.Player

class SectionKeeper {
  private var currentId = 0

  private def newGrass() : GrassSection = {
    currentId += 1
    GrassSection(currentId)
  }
  def union(grassA : GrassSection, grassB : GrassSection) : GrassSection = {
    val grass = newGrass()
    grassA.parent = Some(grass)
    grassB.parent = Some(grass)
    grass.closedCities = grassA.closedCities + grassB.closedCities
    grass
  }

  def newCity() : CitySection = {
    currentId += 1
    CitySection(currentId)
  }

  def union(cityA : CitySection, cityB : CitySection) : CitySection = {
    val city = newCity()
    cityA.parent = Some(city)
    cityB.parent = Some(city)
    city.openEdges = cityA.openEdges + cityB.openEdges
    city
  }

  def newRoad() : RoadSection = ???
  def union(roadA : RoadSection, roadB : RoadSection) : RoadSection = ???

  def newMonastery() : MonasterySection = ???
  def union(roadA : MonasterySection, roadB : MonasterySection) : MonasterySection = ???

  def union(sectionA : BoardSection, sectionB : BoardSection) : BoardSection = ???
  def isOwned(boardSection: BoardSection) : Boolean = ???

  def own(sectionA : BoardSection, player : Player) : Unit = ???
}
