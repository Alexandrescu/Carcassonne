package com.tile

import main.scala.Direction._

abstract class Tile(identifier : String,
                    private var _up : TileEdge,
                    private var _down : TileEdge,
                    private var _left : TileEdge,
                    private var _right :TileEdge, value : Int,
                    private var _orientation : Option[Direction]) {

  private def getSectionsEdge(edge : TileEdge) : Set[Section] = edge match {
    case GrassEdge(s1) => Set(s1)
    case RoadEdge(s1, s2, s3) => Set(s1, s2, s3)
    case CityEdge(s1) => Set(s1)
    case _ => throw new Error("Initialization of tile with non TileEdge not permitted")
  }

  var sections : Set[Section] = Set()
  def getSections(): Set[Section] = sections
  sections = getSectionsEdge(_up) ++ getSectionsEdge(_down) ++ getSectionsEdge(_left) ++getSectionsEdge(_right)

  def orientation_=(aOrientation : Direction): Unit = {
    // I should probably throw an error or remove the if statement
    if(_orientation.isEmpty)
      _orientation = Some(aOrientation)
  }

  def orientation = _orientation.getOrElse(Up)

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

  implicit def intToDirection(value : Int) : Direction = (value % 4) match {
    case 0 => Up
    case 1 => Left
    case 2 => Down
    case 3=> Right
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
}

case class SimpleTile(identifier : String, _up : TileEdge, _down : TileEdge, _left : TileEdge, _right :TileEdge)
  extends Tile(identifier, _up, _down, _left, _right, 1, None)

case class Monastery(identifier : String, _up : TileEdge, _down : TileEdge, _left : TileEdge, _right :TileEdge)
  extends Tile(identifier, _up, _down, _left, _right, 1, None)

case class BannerTile(identifier : String, _up : TileEdge, _down : TileEdge, _left : TileEdge, _right :TileEdge)
  extends Tile(identifier, _up, _down, _left, _right, 2, None)
