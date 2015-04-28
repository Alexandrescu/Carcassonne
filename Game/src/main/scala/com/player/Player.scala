package com.player

class Player(val observer: PlayerObserver) {
  private var followerBag : Set[Follower] = Set()
  for(i <- 1 to 7) followerBag += new Follower(this)

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
    observer.playerUpdate(this)
  }

  def points : Int = _points

  def followerRemoved(follower: Follower): Unit = {
    observer.followerUpdate(follower)
  }
}
