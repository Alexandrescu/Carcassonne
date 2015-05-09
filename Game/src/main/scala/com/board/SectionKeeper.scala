package com.board

import com.tile._

class SectionKeeper {
  def own(move : Move) : Unit = move.toOwnFromTile match {
    case Some(section) =>
      if(section.isOwned) {
        throw new Error("Can't own already owned section")
      }
      else {
        move.player.placeFollower(section, move.place)
      }
    case _ =>
  }

  private def optimizeUnion(sections: Set[Section]) : (Section, Set[Section]) = {
    var maxDepth : Section = sections.toList.head
    for(section <- sections) {
      if(section.treeDepth > maxDepth.treeDepth) {
        maxDepth = section
      }
    }
    (maxDepth, sections - maxDepth)
  }

  def union(thisSection : Section, theSections : Set[Section]) : Unit = {
    // (maxDepth, others)
    val sectionPair = optimizeUnion(theSections + thisSection)

    for(section <- sectionPair._2) {
      union(sectionPair._1, section)
    }
  }

  private def union(sectionA : Section, sectionB : Section): Unit = {
    if(sectionA.treeDepth == sectionB.treeDepth) {
      sectionA.increaseDepth(1)
    }
    sectionB.makeParent(sectionA)
    sectionA.closeInGame()
  }
}
