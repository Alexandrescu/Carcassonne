package com.board.mocks

import com.game.Player

class MockPlayer extends Player{
  override def name: String = "MockPlayer"
  override def hasFollower: Boolean = true
}
