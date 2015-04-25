package com.tile

abstract class Section(val frontEndId : Int) {
  var treeDepth : Int = 1

  protected var _followers : Set[Follower] = Set()
  def isOwned : Boolean
  def addFollowers(newFollowers : Set[Follower]): Unit
  def followers : Set[Follower]

  def updateClose()
}










