package com.tile

class GrassSection(override val frontEndId : Int, initialValue : Int = 1) extends Section(frontEndId, initialValue){
  /**
   * Adds things that stop sections from finishing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: n/a
   * @param x How many of them
   */
  override def addOpen(x: Int): Unit = {}

  override protected def canClose: Boolean = false

  /* Methods which return the points per unit that count at the end */
  override protected def pointsInGame: Int = 0

  /**
   * Removes things that stops sections form closing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: surrounding space
   * @param x How many of them
   */
  override def removeOpen(x: Int): Unit = {}

  override protected def pointsAtEnd: Int = 3

  override def open: Int = 0
}
