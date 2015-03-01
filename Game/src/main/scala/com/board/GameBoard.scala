package com.board

import com.game.Direction
import com.tile._
import Direction.{Direction, _}

class GameBoard(logic: Logic, sectionKeeper: SectionKeeper) extends Board{
  type Dependency = Map[Section, Set[Section]]
  private var board : Map[Place, Tile] = Map()
  private var boardOutline : Map[Place, Set[Section]] = Map()

  override def get(place: Place): Option[Tile] = board.get(place)

  override def getMoves(): Map[(Integer, Integer), Set[Move]] = ???

  override def getBoard(): Map[(Integer, Integer), Tile] = ???

  override def setMove(move: Move): Unit = {
    val moveDep = getMoveDependencies(move)
    if(!isMoveByDependencies(move, moveDep)) {
      throw new Error("Setting an invalid move.")
    }

    solveDependencies(move, moveDep.get)
    updateOutline(move)
  }

  override def isMove(move: Move): Boolean = {
    val moveDep = getMoveDependencies(move)
    isMoveByDependencies(move, moveDep)
  }

  private def updateOutline(move: Move) = {
    // Order matters - should keep track of touched dependencies, but what the heck
    addOutline(move)
    removeOutline(move)
  }
  private def solveDependencies(move: Move, maybeDependency: Dependency): Unit = {
    maybeDependency.foreach{case (thisSection, theSections) => sectionKeeper.union(thisSection, theSections)}
    sectionKeeper.own(move.toOwn, move.player)
  }

  private def addOutline(move: Move) = {
    Direction.values.foreach(direction => getTile(move.place, direction) match {
      case None => {
        // Getting existing outline and removing it to update
        val outlinePlace = add(move.place, direction)
        var outline = boardOutline.getOrElse(outlinePlace, Set())
        boardOutline = boardOutline - move.place

        move.tile.getEdge(direction) match {
          case RoadEdge(_, roadSection, _) => {
            outline = outline + roadSection
            sectionKeeper.addOpen(roadSection)
          }
          case CityEdge(citySection) => {
            outline = outline + citySection
            sectionKeeper.addOpen(citySection)
          }
          case _ => {}
        }

        boardOutline = boardOutline + (outlinePlace -> outline)
      }
      case Some(_) => {}
    })

    move.tile match {
      case Monastery(_, _, _, _, _, move.toOwn) => {
        throw new NotImplementedError("You need to implement the Monastery outline dependency.")
      }
      case _ => {}
    }
  }

  private def removeOutline(move: Move): Unit = {
    boardOutline.get(move.place) match {
      case None => {}
      case Some(sections) => sections.foreach(sectionKeeper.removeOpen(_))
    }
  }

  private def isMoveByDependencies(move : Move, moveDep : Option[Dependency]) : Boolean = {
    moveDep match {
      case None => false
      case Some(dependencies) => move.toOwn match {
        case None => true
        case Some(section) =>
          dependencies.getOrElse(section, Set()).forall(_.isOwned == false) && move.player.hasFollower
      }
    }
  }

  private def getMoveDependencies(move : Move) : Option[Dependency] = {
    // Generating tiles around place

    var mapTiles : Map[Direction, Tile] = Map()
    Direction.values.foreach(direction => getTile(move.place, direction) match {
      case None => {}
      case Some(thatTile) => mapTiles = mapTiles + (direction -> thatTile)
    })

    logic.isMove(move.tile, mapTiles)
  }

  // Auxiliary function
  private implicit def directionToPlace(dir : Direction) : Place = dir match {
    case Up => (0, 1)
    case Down => (0, -1)
    case Left => (-1, 0)
    case Right => (1, 0)
  }
  private def add(place1 : Place, place2 : Place): Place = {
    (place1._1 + place2._1, place1._2 + place2._2)
  }
  private def getTile(place : Place, direction : Direction) : Option[Tile] = {
    get(add(place, direction))
  }
}
