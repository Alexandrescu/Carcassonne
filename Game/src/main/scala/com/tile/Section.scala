package com.tile

import com.player.{Player, Follower}

abstract class Section(val frontEndId : Int, private var _value : Int) {
  var _treeDepth : Int = 1
  def treeDepth : Int = findRoot()._treeDepth
  def increaseDepth(x: Int) = findRoot()._treeDepth += x

  /* Handling ownership */
  protected var _followers : Set[Follower] = Set()
  def isOwned : Boolean = findRoot()._followers.nonEmpty
  def addFollowers(newFollowers : Set[Follower]): Unit = {
    findRoot()._followers ++= newFollowers
  }
  def followers : Set[Follower] = findRoot()._followers

  /* This is to be able to get the 'parent' */
  private var _parent : Option[Section] = None
  def findRoot() : Section = {
    var root = this
    while(root._parent.isDefined) {
      root = root._parent.get
    }
    root
  }
  
  def makeParent(that: Section): Unit = {
    that match {
      case m : MonasterySection =>
        throw new Error("Monasteries have themselves as parents.")
      case _ =>
    }

    val thisRoot = this.findRoot()
    val thatRoot = that.findRoot()

    if(thisRoot == thatRoot) {
      return
    }

    union(thatRoot, thisRoot)
  }

  private def union[A <: Section, B <: Section](parent: A, child: B)(implicit m: Manifest[B]) = parent match {
    case x : B =>
      parent.addFollowers(child.followers)
      parent.addValue(child.value)
      parent.addOpen(child.open)
      (parent, child) match {
        case (parentCity : CitySection, childCity : CitySection) =>
          parentCity.addGrass(childCity.getGrass)
        case (parentGrass : GrassSection, childGrass : GrassSection) =>
          parentGrass.addClosedCity(childGrass.getClosedCities)
        case _ =>
      }
      child._parent = Some(parent)
    case _ =>
      throw new MatchError("Trying to union different kinds of sections.")
  }

  /**
   * Adds things that stop sections from finishing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: n/a
   * @param x How many of them
   */
  def addOpen(x : Int) : Unit = {
    findRoot().addOpenInternal(x)
  }

  /**
   * Removes things that stops sections form closing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: surrounding space
   * @param x How many of them
   */
  def removeOpen(x : Int) : Unit = {
    findRoot().removeOpenInternal(x)
  }

  def open : Int = findRoot().openInternal

  /**
   * Helpers
   */

  protected def addOpenInternal(x : Int)
  protected def removeOpenInternal(x : Int)
  protected def openInternal : Int
  protected def canCloseInternal : Boolean

  /**
   * Value is the value of the section:
   * City: Number of tiles
   * Road: Number of tiles
   * Grass: Number of adjacent cities
   * Monastery: N/A -> it depends on the surroundings
   */

  def value = findRoot()._value
  def addValue(x : Int) = findRoot()._value += x
  def removeValue(x : Int) = findRoot()._value -= x

  /* Methods which return the points per unit that count at the end */
  def pointsInGame : Int
  def pointsAtEnd : Int
  protected def canClose : Boolean = findRoot().canCloseInternal

  private var _closed : Boolean = false
  def closed : Boolean = findRoot()._closed

  def closeInGame(): Unit = {
    val root = findRoot()
    if(!root._closed && root.canClose) {
      root._closed = true

      root match {
        case city : CitySection => city.valueGrass()
        case _ =>
      }

      root.closeWithPoints(root.pointsInGame)
    }
  }

  def closeAtEnd(): Unit = {
    val root = findRoot()
    if(!root._closed) {
      root match {
        case grass : GrassSection => grass.valueCities()
        case _ =>
      }
      root._closed = true
      root.closeWithPoints(root.pointsAtEnd)
    }
  }

  private def closeWithPoints(points : Int): Unit = {
    val root = findRoot()
    val maj = majority(root._followers)
    var scored:Set[Player] = Set()

    for(follower <- root._followers) {
      for(player <- maj) {
        if(player == follower.player && !scored.contains(player)) {
          follower.take(points * value)
          scored += player
        }
      }

      if(follower.isPlaced) {
        follower.take(0)
      }
    }
  }

  private def majority(followerSet : Set[Follower]) : Set[Player] = {
    var maxCounter = 0
    var maxFollowers : Map[Player, Int] = Map()
    for(follower <- followerSet) {
      val player = follower.player
      val counter = maxFollowers.getOrElse(player, 0) + 1

      maxFollowers -= player
      maxFollowers += (player -> counter)

      if(maxCounter < counter) {
        maxCounter = counter
      }
    }

    var maxPlayers : Set[Player] = Set()
    for((player, counter) <- maxFollowers) {
      if(counter == maxCounter) maxPlayers += player
    }

    maxPlayers
  }
}










