package com.game

abstract class Follower
case class PlayerFollower(id : Integer) extends Follower
case class Section(id : Integer) extends Follower
