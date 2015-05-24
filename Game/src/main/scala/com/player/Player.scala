package com.player

import com.tile._

class Player(val slot : Int, private var observers: Set[PlayerObserver] = Set()) {
  private var followerBag : Set[Follower] = Set()
  for(i <- 1 to 7) followerBag += new Follower(this)

  def placeFollower(section : Section, place : (Int, Int)) : Unit = {
    for(follower <- followerBag) {
      if(!follower.isPlaced) {
        follower.place = place
        follower.section = section
        follower.sectionType = section match {
          case x : GrassSection => 0
          case x : CitySection => 1
          case x : RoadSection => 2
          case x : MonasterySection => 3
          case _ => -1
        }
        section.addFollowers(Set(follower))
        return
      }
    }
  }

  def followers : Int = {
    var result = 0
    for(follower <- followerBag) {
      if(!follower.isPlaced) {
        result += 1
      }
    }
    result
  }

  def hasFollower : Boolean = followers > 0

  /* Handling points */
  private var _points : Int = 0

  private def addPoints(i: Int): Unit = {
    _points += i
  }

  def points : Int = _points

  /* Follower events */
  def followerRemoved(follower: Follower, pointsAdded : Int): Unit = {
    addPoints(pointsAdded)
    observers.foreach(_.followerUpdate(follower))
  }

  /* Observing the player */
  def registerObserver(observer: PlayerObserver) = {
    observers += observer
  }

  def removeObserver(observer: PlayerObserver) = {
    observers -= observer
  }
}
