package com.ai.uct

import com.board.Direction.Direction
import com.board.Move

import scala.collection.mutable.ArrayBuffer

class Node(private val round : Int) {
  private var wins: Double = 0
  var visits: Int = 0

  private var pDelta : Double = 0
  private var fDelta : Double = 0

  private var _children: ArrayBuffer[Node] = new ArrayBuffer()
  def children : Array[Node] = _children.toArray
  def children_= (newChildren : ArrayBuffer[Node]) : Unit = _children = newChildren
  def hasChildren = _children.nonEmpty

  // (1, 1)
  private val pointWorth : Double = 4
  private val followerWorth : Double = 7 // * ((85 - round) / 72)
  private val winWorth : Double = 1
  private val total : Double = pointWorth + followerWorth + winWorth

  private val pRatio = pointWorth / total
  private val fRatio = followerWorth / total
  private val wRatio = winWorth / total

  def stats(pointDelta: Double, followerDelta: Double) = {
    pDelta = pointDelta
    fDelta = followerDelta + 7
  }

  def update(value : Double): Unit = {
    visits += 1
    wins += value
  }

  def getBest: Node = {
    var best : Option[Node] = None
    var bestValue : Double = -1

    for(child <- _children) {
      if(child.winRate > bestValue) {
        bestValue = child.winRate
        best = Some(child)
      }
    }

    if(best isEmpty) {
      throw UninitializedFieldError("No children.")
    }

    best.get
  }

  private def sigmoidFunction(value : Double) : Double = 1 - Math.pow(1.618, -value)

  def winRate : Double = {
    //println(s"PointDelta $pDelta and followerDelta $fDelta")
    if(this.visits > 0) {
      val result :Double= (wins / visits.toDouble) * wRatio + sigmoidFunction(pDelta) * pRatio + sigmoidFunction(fDelta) * fRatio
      //println(s"Result $result")
      result
    }
    else 0
  }

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
}
