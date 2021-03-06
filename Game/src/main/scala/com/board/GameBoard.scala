package com.board

import com.board.Direction.{Direction, _}
import com.player.Player
import com.tile._

class GameBoard(logic: Logic, sectionKeeper: SectionKeeper) extends Board{
  type Dependency = Map[Section, Set[Section]]
  /**
   * board : keeps the set of placed tiles
   * board(i,j) : tile that is in the game at position (i,j)
   *
   * boardOutline: Keeps track of the sections that need updated when a
   * tile is added there.
   * boardOutline(i, j): Places that get points when a tile is placed there.
   *
   * tileOutline: Knows where the next tiles can be placed
   */
  private var board : Map[Place, Tile] = Map()
  private var boardOutline : Map[Place, Set[Section]] = Map()
  private var tileOutline : Set[Place] = Set((0, 0))

  override def get(place: Place): Option[Tile] = board.get(place)

  override def getMoves(tile : Tile, player : Player): Set[PossibleMove] = {
    var possibleMoves : Set[PossibleMove] = Set()
    var sections : Set[Option[Section]] = if(player.hasFollower) tile.getSections().map(Some(_)) else Set()
    sections = sections + None

    tileOutline.foreach{place => Direction.values.foreach(direction => {
      // Moving tile in different directions
      var toOwnList: List[Option[Section]] = List()
      tile.orientation = direction
      sections.foreach(someSection => {
        val move = new Move(tile, place, someSection , player)
        if(isMove(move)) {
          toOwnList ::= someSection
        }
      })

      if(toOwnList.nonEmpty) {
        possibleMoves += new PossibleMove(direction, place, toOwnList)
      }
    })}

    possibleMoves
  }

  override def getBoard(): Map[Place, Tile] = board

  override def setMove(move: Move): Unit = {
    val moveDep = getMoveDependencies(move)
    if(!isMoveByDependencies(move, moveDep)) {
      throw new Error("Setting an invalid move.")
    }

    solveDependencies(move, moveDep.get)
    updateOutline(move)
    board = board + (move.place -> move.tile)
  }

  override def isMove(move: Move): Boolean = {
    val moveDep = getMoveDependencies(move)
    isMoveByDependencies(move, moveDep)
  }

  private def updateTileOutline(move: Move) = {
    tileOutline -= move.place
    Direction.values.foreach(direction => {
      val newPlace = add(move.place, direction)
      if(get(newPlace).isEmpty) {
        tileOutline += newPlace
      }
    })
  }

  private def updateOutline(move: Move) = {
    // Order matters - should keep track of touched dependencies, but what the heck
    addOutline(move)
    removeOutline(move)
    updateTileOutline(move)
    move.tile.getSections().foreach(section => section.closeInGame())
  }
  private def solveDependencies(move: Move, maybeDependency: Dependency): Unit = {
    sectionKeeper.own(move)
    for((section, sectionSet) <- maybeDependency) {
      sectionKeeper.union(section, sectionSet)
    }
  }

  private def addOutline(move: Move) = {
    Direction.values.foreach(f = direction => getTile(move.place, direction) match {
      case None =>
        // Getting existing outline and removing it to update
        val outlinePlace = add(move.place, direction)
        var outline = boardOutline.getOrElse(outlinePlace, Set())

        move.tile.getEdge(direction) match {
          case RoadEdge(_, roadSection : RoadSection, _) =>
            outline = outline + roadSection
            roadSection.addOpen(1)
          case CityEdge(citySection : CitySection) =>
            outline = outline + citySection
            citySection.addOpen(1)
          case _ =>
        }

        boardOutline = boardOutline - outlinePlace
        if(outline.nonEmpty)
          boardOutline = boardOutline + (outlinePlace -> outline)
      case Some(_) =>
    })

    move.toOwnFromTile match {
      case Some(playerSection : MonasterySection) =>
        // these `` allow scala to know you are interpreting the value as a constant
        move.tile match {
          case Monastery(_, _, _, _, _, `playerSection`) =>
            // Adding all 8 surrounding places to the outline
            val monasteryOutline: Set[Place] = Set(
              add(add(move.place, Up), Right),
              add(add(move.place, Up), Left),
              add(add(move.place, Down), Right),
              add(add(move.place, Down), Left)
            ) ++ Direction.values.map(add(move.place, _)).toSet

            monasteryOutline.foreach(place => get(place) match {
              case Some(_) =>
                playerSection.removeOpen(1)
              case None =>
                var outline = boardOutline.getOrElse(place, Set())
                outline = outline + playerSection

                boardOutline = boardOutline - place
                boardOutline = boardOutline + (place -> outline)
            })
          case _ =>
        }
      case _ =>
    }
  }

  private def removeOutline(move: Move): Unit = {
    boardOutline.get(move.place) match {
      case None =>
      case Some(sections) =>
        for(section <- sections) {
          section.removeOpen(1)
          section.closeInGame()
        }
    }
    boardOutline -= move.place
  }

  private def isMoveByDependencies(move : Move, moveDep : Option[Dependency]) : Boolean = {
    moveDep match {
      case None => false
      case Some(dependencies) => move.toOwnFromTile match {
        case None => true
        case Some(section) =>
          dependencies.getOrElse(section, Set()).forall(_.isOwned == false) && move.player.hasFollower
      }
    }
  }

  private def getMoveDependencies(move : Move) : Option[Dependency] = {
    // Generating tiles around place
    var mapTiles : Map[Direction, Tile] = Map()
    Direction.values.foreach(f = direction => getTile(move.place, direction) match {
      case None =>
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
