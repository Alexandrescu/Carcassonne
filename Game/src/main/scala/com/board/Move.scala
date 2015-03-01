package com.board

import com.game.Player
import com.tile.{Section, Tile}

class Move(val tile : Tile, val place : (Int, Int), val toOwn : Option[Section], val player : Player)
