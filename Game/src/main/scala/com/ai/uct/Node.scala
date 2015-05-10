package com.ai.uct

import com.board.Direction.Direction
import com.board.Move

class Node {
  def getBest: Node = {
    var child = this.child
    var best : Option[Node] = None
    var bestValue = -1
    while(child.isDefined) {
      if(child.get.visits > bestValue) {
        bestValue = child.get.visits
        best = child
      }
      child = child.get.sibling
    }
    best.get
  }

  var wins: Double = 0
  var visits: Int = 0

  private var _orientation : Option[Direction] = None
  private var _move: Option[Move] = None
  def move_=(move : Move): Unit = {
    _orientation = Some(move.tile.orientation)
    _move = Some(move)
  }
  def move : Option[Move] = {
    if(_move isDefined) {
      _move.get.tile.orientation = _orientation.get
    }
    _move
  }
  var child: Option[Node] = None
  var sibling: Option[Node] = None

  def winRate : Double = {
    if(this.visits > 0) {
      wins / visits.toDouble
    }
    else 0
  }

  def update(value : Double): Unit = {
    visits += 1
    wins += value
  }
}
