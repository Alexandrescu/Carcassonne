package com.tile

import com.player.Follower

class MonasterySection(override val frontEndId : Int) extends Section(frontEndId) {
  private var surroundTiles : Int = 0

  def addOpen(x : Int = 1) : Unit = {}

  def removeOpen(x : Int = 1) : Unit = {
    surroundTiles += x
  }

  override def isOwned: Boolean = followers.nonEmpty

  override def addFollowers(newOwners: Set[Follower]): Unit = {
    if(newOwners.size > 1)
      throw new Exception("Can't have more than one owner on Monastery")
    _followers = newOwners
  }

  override def followers: Set[Follower] = _followers

  override def findRoot(): Section = this

  // Can close when it's surrounded
  override protected def canClose(): Boolean = surroundTiles == 8

  override protected def closeInGame(): Unit = testClosing()

  override protected def closeAtEnd(): Unit = testClosing()

  private def testClosing(): Unit = {
    if(_followers.size > 1) throw new Error("Monastery should only have one owner")
    closeWithPoints(surroundTiles + 1)
  }

}
