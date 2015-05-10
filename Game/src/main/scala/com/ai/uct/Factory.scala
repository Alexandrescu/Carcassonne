package com.ai.uct

import com.board.{SectionKeeper, StandardLogic}

object Factory {
  val keeper = new SectionKeeper
  val logic = new StandardLogic
}
