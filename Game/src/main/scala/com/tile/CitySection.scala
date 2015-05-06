package com.tile

class CitySection(override val frontEndId : Int, initialValue : Int = 1) extends Section(frontEndId, initialValue){
  private var _openEdges = 0
  /**
   * Adds things that stop sections from finishing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: n/a
   * @param x How many of them
   */
  override def addOpen(x: Int): Unit = _openEdges += x

  override protected def canClose: Boolean = _openEdges == 0

  /* Methods which return the points per unit that count at the end */
  override protected def pointsInGame: Int = 2

  /**
   * Removes things that stops sections form closing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: surrounding space
   * @param x How many of them
   */
  override def removeOpen(x: Int): Unit = _openEdges -= x

  override protected def pointsAtEnd: Int = 1

  override def open: Int = _openEdges

  private var surroundingGrass : Set[GrassSection] = Set()
  def addGrass(grass : Set[GrassSection]): Unit = {
    surroundingGrass ++= grass
  }

  def getGrass : Set[GrassSection] = surroundingGrass

  def valueGrass(): Unit = {
    for(grass <- surroundingGrass) {
      grass.addValue(1)
    }
  }
}
