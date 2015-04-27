package com.game

import com.board.{SectionKeeper, StandardLogic, GameBoard}

object GameFactory {
  def standardGame(callEndOfGame : () => Unit) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new StandardTileBag, callEndOfGame)

  def testGame(callEndOfGame : () => Unit) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new TestTileBag, callEndOfGame)
}
