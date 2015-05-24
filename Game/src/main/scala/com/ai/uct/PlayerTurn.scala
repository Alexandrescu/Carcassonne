package com.ai.uct

import com.player.Player

class PlayerTurn(number : Int) {
  def isCurrent(i: Int): Boolean = _current == i

  val pointRange = 50
  var sorted = Array[Player]()
  def winResult(slot: Int): Double = {
    if(sorted.length == 0) {
      sorted = players.sortBy(player => -player.points)
    }

    for(i <- 0 until sorted.length) {
      if(sorted(i).slot == slot) {
        val diff =
          if(i == 0) {
            Math.min(sorted(i).points - sorted(sorted.length - 1).points, 50)
          }
          else {
            Math.max(sorted(i).points - sorted(0).points, -50)
          }

        return 0.5 + (diff/100)
      }
    }
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
