package com.board

import com.game.Direction.Direction
import com.game.Player
import com.tile.{Section, Tile}

class Move(val tile : Tile, val place : (Int, Int), val toOwnFromTile : Option[Section], val player : Player)
class PossibleMove(val direction : Direction, val place : (Int, Int), val toOwnFromTile : Option[Section])