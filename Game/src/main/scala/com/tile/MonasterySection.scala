package com.tile

class MonasterySection(override val frontEndId : Int) extends Section(frontEndId) {
  private var tileCount : Int = 0

  def addOpen() : Unit = {
    tileCount += 1
  }

  def removeOpen() : Unit = {
    tileCount -= 1
  }

  override def isOwned: Boolean = followers.nonEmpty

  override def addFollowers(newOwners: Set[Follower]): Unit = {
    if(newOwners.size > 1)
      throw new Exception("Can't have more than one owner on Monastery")
    _followers = newOwners
  }

  override def followers: Set[Follower] = _followers

  override def closeSection(): Unit = {
    if(tileCount == 0) {
      var flagIterated = false
      for(follower <- _followers) {
        if(flagIterated) throw new Error("Monastery should only have one owner")
        flagIterated = true

        follower.player.addPoints(9)
        follower.take()
      }
    }
  }

  override def findRoot(): Section = this
}
