package com.player

trait PlayerObserver {
  def followerUpdate(follower : Follower)
  def playerUpdate(player : Player)
}
