package main.scala

import main.scala.Direction._


abstract class Tile(identifier : String,
                    _up : TileEdge, _down : TileEdge, _left : TileEdge, _right :TileEdge, value : Int,
                    private var _orientation : Option[Direction]) {

  def orientation_=(aOrientation : Direction): Unit = {
    // I should probably throw an error or remove the if statement
    if(_orientation.isEmpty)
      _orientation = Some(aOrientation)
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

  implicit def intToDirection(value : Int) : Direction = (value % 4) match {
    case 0 => Up
    case 1 => Left
    case 2 => Down
    case 3=> Right
  }

  private def getEdge(direction : Direction) : TileEdge = {
    val offset : Int = orientation.getOrElse(Up).toInt
    val directionValue = direction.toInt + offset

    getTileEdge(directionValue)
  }

  def getTileEdge(direction : Direction) : TileEdge = direction match {
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
