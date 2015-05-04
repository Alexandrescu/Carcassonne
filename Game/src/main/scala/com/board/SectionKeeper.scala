package com.board

import com.tile._

class SectionKeeper {
  def removeOpen(section: Section): Unit = section match {
    case citySection : CitySection => citySection.removeOpen()
    case roadSection : RoadSection => roadSection.removeOpen()
    case monasterySection : MonasterySection => monasterySection.removeOpen()
    case _ => throw new NoSuchElementException("There should only be these type of sections")
  }

  def union(grassA : GrassSection, grassB : GrassSection) : Unit = {
    if(grassA.treeDepth == grassB.treeDepth){
      grassA.treeDepth += 1
    }
    grassA.addFollowers(grassB.followers)

    grassA.addClosedCities(grassB.closedCities())
    grassB.parent(grassA)
  }

  def union(cityA : CitySection, cityB : CitySection) : Unit = {
    if(cityA.treeDepth == cityB.treeDepth) {
      cityA.treeDepth += 1
    }
    cityA.addFollowers(cityB.followers)

    cityA.addOpen(cityB.openEdges)
    cityA.addTiles(cityB.tileCount())
    cityA.addGrass(cityB.getGrass())
    cityB.parent(cityA)
  }

  def union(roadA : RoadSection, roadB : RoadSection) : Unit = {
    if(roadB.treeDepth == roadA.treeDepth) {
      roadA.treeDepth += 1
    }
    roadA.addFollowers(roadB.followers)

    roadA.addOpen(roadB.openEdges)
    roadA.addTiles(roadB.tileCount())
    roadB.parent(roadA)
  }

  def own(move : Move) : Unit = move.toOwnFromTile match {
    case Some(section) =>
      if(section.isOwned) {
        throw new Error("Can't own already owned section")
      }
      else {
        section.addFollowers(Set(move.player.getFollower(section, move.place)))
      }
    case _ =>
  }

  def optimizeUnion(sections: Set[Section]) : (Section, Set[Section]) = {
    var maxDepth : Section = sections.toList.head
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
