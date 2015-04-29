package com.player

import com.tile.Section

class Player(val observer: PlayerObserver, val slot : Int) {
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

  // Handling points
  private var _points : Int = 0

  def addPoints(i: Int): Unit = {
    _points += i
    observer.playerUpdate(this)
  }

  def points : Int = _points

  def followerRemoved(follower: Follower): Unit = {
    observer.followerUpdate(follower)
  }
}
