package com.ai.uct

import com.board.Direction.Direction
import com.board.Move

class Node {
  private var pDelta : Double = 0
  private var fDelta : Double = 0
  def stats(pointDelta: Double, followerDelta: Double) = {
    pDelta = pointDelta
    fDelta = followerDelta + 7
  }


  def getBest: Node = {
    var child = this.child
    var best : Option[Node] = None
    var bestValue : Double = -1
    while(child.isDefined) {
      if(child.get.winRate > bestValue) {
        bestValue = child.get.winRate
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

  val pointWorth : Double = 10
  val followerWorth : Double = 3
  val winWorth : Double = 1
  val total : Double = pointWorth + followerWorth + winWorth

  val pRatio = pointWorth / total
  val fRatio = followerWorth / total
  val wRatio = winWorth / total

  private def sigmoidFunction(value : Double) : Double = 1 - Math.pow(1.618, -value)

  def winRate : Double = {
    println(s"PointDelta $pDelta and followerDelta $fDelta")
    if(this.visits > 0) {
      val result :Double= (wins / visits.toDouble) * wRatio + sigmoidFunction(pDelta) * pRatio + sigmoidFunction(fDelta) * fRatio
      println(s"Result $result")
      result
    }
    else 0
  }

  def update(value : Double): Unit = {
    visits += 1
    wins += value
  }
}
