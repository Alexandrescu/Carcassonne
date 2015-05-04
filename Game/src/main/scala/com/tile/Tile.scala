package com.tile

import com.board.Direction._

abstract class Tile(val identifier : String,
                    private var _up : TileEdge,
                    private var _down : TileEdge,
                    private var _left : TileEdge,
                    private var _right :TileEdge, value : Int) {

  private var _orientation : Direction = Up

  /**
   * @param edge
   * @return The set of updated sections on that tile edge.
   */
  private def getSectionsEdge(edge : TileEdge) : Set[Section] = edge match {
    case GrassEdge(s1) => Set(s1)
    case RoadEdge(s1, s2, s3) => Set(s1, s2, s3)
    case CityEdge(s1) => Set(s1)
    case _ => throw new Error("Initialization of tile with non TileEdge not permitted")
  }

  protected var sections : Set[Section] = Set()
  def getSections(): Set[Section] = sections
  sections = getSectionsEdge(_up) ++ getSectionsEdge(_down) ++ getSectionsEdge(_left) ++getSectionsEdge(_right)

  def getSectionById(id : Int) : Option[Section] = {
    for(section <- sections) {
      if(section.frontEndId == id)
        return Some(section)
    }
    None
  }

  def orientation_=(aOrientation : Direction): Unit = {
    // I should probably throw an error or remove the if statement
    _orientation = aOrientation
  }

  def orientation = _orientation

  def up = {getEdge(Up)}
  def down = {getEdge(Down)}
  def left = {getEdge(Left)}
  def right = {getEdge(Right)}

  implicit def directionToInt(direction : Direction) : Int = direction match {
    case Up => 0
    case Left => 1
    case Down => 2
    case Right => 3
  }

  implicit def intToDirection(value : Int) : Direction = (value + 4) % 4 match {
    case 0 => Up
    case 1 => Left
    case 2 => Down
    case 3 => Right
  }

  def getEdge(direction : Direction) : TileEdge = {
    val offset : Int = orientation.toInt
    val directionValue = direction.toInt + offset

    getTileEdge(directionValue)
  }

  private def getTileEdge(direction : Direction) : TileEdge = direction match {
    case Up => _up
    case Down => _down
    case Right => _right
    case Left => _left
  }

  /**
   * Updating the grass for each city section.
   */

  private def getBeforeEdge(direction: Direction) : TileEdge = {
    getEdge(direction - 1)
  }

  private def getAfterEdge(direction : Direction) : TileEdge = {
    getEdge(directionToInt(direction)+ 1)
  }

  private def addGrass(direction: Direction): Unit = {
    getEdge(direction) match {
      case CityEdge(c) =>
        getBeforeEdge(direction) match {
          case GrassEdge(g) => c.addGrass(Set(g))
          case RoadEdge(_, _, g) => c.addGrass(Set(g))
          case _ =>
        }

        getAfterEdge(direction) match {
          case GrassEdge(g) => c.addGrass(Set(g))
          case RoadEdge(g, _, _) => c.addGrass(Set(g))
          case _ =>
        }
      case _ =>
    }
  }

  // Initialising the grass
  for(i <- 0 to 3) addGrass(i)
}

case class SimpleTile(override val identifier : String, _up : TileEdge, _down : TileEdge, _left : TileEdge, _right :TileEdge)
  extends Tile(identifier, _up, _down, _left, _right, 1)

case class Monastery(override val identifier : String, _up : TileEdge, _down : TileEdge, _left : TileEdge, _right :TileEdge, monastery: MonasterySection)
  extends Tile(identifier, _up, _down, _left, _right, 1) {
  sections = sections + monastery
}

case class BannerTile(override val identifier : String, _up : TileEdge, _down : TileEdge, _left : TileEdge, _right :TileEdge)
  extends Tile(identifier, _up, _down, _left, _right, 2)
