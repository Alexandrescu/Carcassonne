package com.tile

import com.player.{Player, Follower}

abstract class Section(val frontEndId : Int) {
  var treeDepth : Int = 1

  protected var _followers : Set[Follower] = Set()
  def isOwned : Boolean
  def addFollowers(newFollowers : Set[Follower]): Unit
  def followers : Set[Follower]

  def closeSection()

  def findRoot() : Section

  private var closed : Boolean = false

  def finishSection(): Unit = {
    val root = findRoot()
    if(!root.closed) {
      root.closed = true
      root.closeSection()
    }
  }
  
  def removeFollowers(followerSet : Set[Follower]): Unit = {
    for(follower <- followerSet) {
      follower.take()
    }
  }

  def majority(followerSet : Set[Follower]) : Set[Player] = {
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










