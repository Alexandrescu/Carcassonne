package com.ai

import com.board.PossibleMove
import com.server.json.GameMove
import com.tile.Tile

trait AI {
  def getMove(tile : Tile, moves: Set[PossibleMove]) : GameMove
}
