package com.player

class Player(val slot : Int, var name : String, var uuid : String, private var _token : String) {
  def token : String = _token

  private var followerBag : Set[Follower] = Set()
  for(i <- 1 to 7) {
    followerBag += new Follower(this)
  }

  def getFollower : Follower = {
    for(follower <- followerBag) {
      if(!follower.isPlaced) {
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
  }

  def points : Int = _points
}
