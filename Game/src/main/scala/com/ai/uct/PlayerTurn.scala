package com.ai.uct

import com.player.Player

class PlayerTurn(number : Int) {
  def winResult(slot: Int): Double = {
    val sorted = players.sortBy(player => -player.points)

    if(sorted(0).slot == slot) return 1
    0
  }

  val players : Array[Player] = (for (i <- 0 until number) yield new Player(i)).toArray

  def getBySlot(i: Int) = {
    players(i)
  }

  private var _current : Int = 0

  def setCurrent(x : Int): Unit = {
    _current = x
  }

  def current : Player = players(_current)

  def next() : Player = {
    _current = (_current + 1) % number
    players(_current)
  }

  def currentSlot : Int = _current
}
