package com.player

import com.tile.Section

class Player(val slot : Int, private var observers: Set[PlayerObserver] = Set()) {
  private var followerBag : Set[Follower] = Set()
  for(i <- 1 to 7) followerBag += new Follower(this)

  def getFollower(section : Section, place : (Int, Int)) : Follower = {
    for(follower <- followerBag) {
      if(!follower.isPlaced) {
        follower.place = place
        follower.section = section
        return follower
      }
    }
    null
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
