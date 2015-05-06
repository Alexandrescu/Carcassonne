package com.tile

class RoadSection(override val frontEndId : Int, initialValue : Int = 1) extends Section(frontEndId, initialValue){
  private var _openEdges : Int = 0

  override protected def canClose: Boolean = _openEdges == 0

  /**
   * Adds things that stop sections from finishing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: n/a
   * @param x How many of them
   */
  override def addOpen(x: Int): Unit = _openEdges += x

  /**
   * Removes things that stops sections form closing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: surrounding space
   * @param x How many of them
   */
  override def removeOpen(x: Int): Unit = _openEdges -= x

  /* Methods which return the points per unit that count at the end */
  override protected def pointsInGame: Int = 1

  override protected def pointsAtEnd: Int = 1

  override def open: Int = _openEdges
}
