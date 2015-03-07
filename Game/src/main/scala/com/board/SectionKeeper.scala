package com.board

import com.game.Player
import com.tile._

class SectionKeeper {
  def removeOpen(section: Section): Unit = section match {
    case citySection : CitySection => removeOpen(citySection)
    case roadSection : RoadSection => removeOpen(roadSection)
    case monasterySection : MonasterySection => removeOpen(monasterySection)
    case _ => throw new NoSuchElementException("There should only be these type of sections")
  }

  def removeOpen(citySection: CitySection) : Unit = {
    citySection.openEdges -= 1
  }

  def removeOpen(roadSection: RoadSection) : Unit = {
    roadSection.openEdges -= 1
  }

  def removeOpen(monasterySection: MonasterySection): Unit = {
    monasterySection.tileCount -= 1
  }

  def addOpen(citySection: CitySection) = {
    citySection.openEdges += 1
  }

  def addOpen(roadSection: RoadSection) = {
    roadSection.openEdges += 1
  }

  def addOpen(monasterySection: MonasterySection) = {
    monasterySection.tileCount += 1
  }

  def union(grassA : GrassSection, grassB : GrassSection) : Unit = {
    grassB.parent = Some(grassA)
    if(grassA.treeDepth == grassB.treeDepth){
      grassA.treeDepth += 1
    }
    grassA.addOwners(grassB.owners)

    grassA.closedCities ++= grassB.closedCities
  }

  def union(cityA : CitySection, cityB : CitySection) : Unit = {
    cityB.parent = Some(cityA)
    if(cityA.treeDepth == cityB.treeDepth) {
      cityA.treeDepth += 1
    }
    cityA.addOwners(cityB.owners)

    cityA.openEdges += cityB.openEdges
    cityA.tileCount += cityB.tileCount
    cityA.adjacentGrass = cityA.adjacentGrass.map(grass => grass.findRoot())
    cityA.adjacentGrass ++= cityB.adjacentGrass.map(grass => grass.findRoot())
  }

  def union(roadA : RoadSection, roadB : RoadSection) : Unit = {
    roadB.parent = Some(roadA)
    if(roadB.treeDepth == roadA.treeDepth) {
      roadA.treeDepth += 1
    }
    roadA.addOwners(roadB.owners)

    roadA.openEdges += roadB.openEdges
    roadA.tileCount += roadB.tileCount
  }

  def own(sectionOption : Option[Section], player : Player) : Unit = sectionOption match {
    case Some(section) =>
      if(section.isOwned) {
        throw new Error("Can't own already owned section")
      }
      else {
        section.addOwners(Set(player))
      }
    case _ =>
  }

  def optimizeUnion(sections: Set[Section]) : (Section, Set[Section]) = {
    var maxDepth : Section = new Section()
    sections.foreach(section => if(section.treeDepth > maxDepth.treeDepth) maxDepth = section)
    (maxDepth, sections - maxDepth)
  }

  def union(thisSection : Section, theSections : Set[Section]) : Unit = {
    val sectionPair = optimizeUnion(theSections + thisSection)
    val sections = sectionPair._2

    sectionPair._1 match {
      case roadSection : RoadSection =>
        sections.foreach{
          case otherRoadSection : RoadSection => union(roadSection, otherRoadSection)
          case _ => throw new MatchError("Going to union road with other section. Wrong!")
        }
      case citySection : CitySection =>
        sections.foreach{
          case otherCitySection : CitySection => union(citySection, otherCitySection)
          case _ => throw new MatchError("Going to union city with other section. Wrong!")
        }
      case grassSection : GrassSection =>
        sections.foreach {
          case otherGrassSection : GrassSection => union(grassSection, otherGrassSection)
          case _ => throw new MatchError("Going to union grass with other section. Wrong!")
        }
    }
  }

}
