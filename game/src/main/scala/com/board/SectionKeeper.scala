package com.board

import com.game.Player
import com.tile._

class SectionKeeper {
  def removeOpen(section: Section): Unit = ???

  def addOpen(section: CitySection) = ???

  def addOpen(section: RoadSection) = ???

  private var currentId = 0

  private def newGrass() : GrassSection = {
    currentId += 1
    GrassSection()
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
    CitySection()
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

  def union(sectionA : Section, sectionB : Section) : Section = ???
  def isOwned(boardSection: Section) : Boolean = ???

  def own(sectionA : Option[Section], player : Player) : Unit = ???

  def union(thisSection : Section, theSections : Set[Section]) : Unit = ???

}
