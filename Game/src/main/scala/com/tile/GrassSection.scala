package com.tile

class GrassSection(override val frontEndId : Int, initialValue : Int = 0) extends Section(frontEndId, initialValue){
  /**
   * Adds things that stop sections from finishing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: n/a
   * @param x How many of them
   */
  override def addOpenInternal(x: Int): Unit = {}

  override protected def canCloseInternal: Boolean = false

  /* Methods which return the points per unit that count at the end */
  override def pointsInGame: Int = 0

  /**
   * Removes things that stops sections form closing:
   * City: open edges
   * Road: open edges
   * Grass: n/a
   * Monastery: surrounding space
   * @param x How many of them
   */
  override def removeOpenInternal(x: Int): Unit = {}

  override def pointsAtEnd: Int = 3

  override def openInternal: Int = 0

  private var cities : Set[CitySection] = Set()
  def addClosedCity(citySection: Set[CitySection]): Unit = {
    cities ++= citySection
  }

  def getClosedCities : Set[CitySection] = cities

  def valueCities() = addValue(cities.size)
}
