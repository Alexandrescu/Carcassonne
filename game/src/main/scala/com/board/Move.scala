package com.board

import com.tile.{Section, Tile}
import main.scala.Player

class Move(val tile : Tile, val place : (Int, Int), val toOwn : Option[Section], val player : Player)
