package com.board

import main.scala.Player

class DisjointSetUnionKeeper extends UnionKeeper{
  private val sectionKeeper : SectionKeeper = ???

  private val parent : Array[Int] = new Array[Int](1024)
  private val rank : Array[Int] = new Array[Int](1024)
  private var currentSection = 0
  private var owned : Map[Int, List[Player]] = Map()

  private def findRoot(node : Int) : Int = {
    if(parent(node) == 0) {
      return node
    }
    parent(node) = findRoot(parent(node))
    return parent(node)
  }

  override def add(section: TileSection): BoardSection = {
    currentSection += 1
    BoardSection(currentSection)
  }

  override def base: BoardSection = BoardSection(0)

  override def isOwned(boardSection: Int): Boolean = {
    owned.contains(findRoot(boardSection)) == false
  }

  override def isOwned(boardSection: BoardSection): Boolean = isOwned(boardSection.id)

  override def union(boardSectionA: BoardSection, boardSectionB: BoardSection): BoardSection = {
    val setA = findRoot(boardSectionA.id)
    val setB = findRoot(boardSectionB.id)

    val ownersA = owned.getOrElse(setA, List())
    val ownersB = owned.getOrElse(setB, List())

    if(rank(setA) > rank(setB)) {
      parent(setB) = setA
    }
    else {
      parent(setA) = setB
    }

    if(rank(setA) == rank(setB)) {
      rank(setB) += 1
    }

    owned = owned - setA
    owned = owned - setB

    val newOwners = ownersA ++ ownersB
    if(!newOwners.isEmpty) {
      owned = owned + (findRoot(setA) -> newOwners)
    }

    BoardSection(findRoot(setA))
  }

  override def own(boardSection: BoardSection, player: Player): Boolean = {
    if(isOwned(boardSection)) {
      return false
    }
    owned = owned + (boardSection.id -> List(player))
    true
  }
}
