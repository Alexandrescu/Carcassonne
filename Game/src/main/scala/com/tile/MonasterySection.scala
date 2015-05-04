package com.tile

import com.player.Follower

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

  override def findRoot(): Section = this

  override protected def canClose(): Boolean = tileCount == 0

  override protected def closeInGame(): Unit = testClosing()

  override protected def closeAtEnd(): Unit = testClosing()

  private def testClosing(): Unit = {
    if(_followers.size > 1) throw new Error("Monastery should only have one owner")
    closeWithPoints(9 - tileCount)
  }

}
