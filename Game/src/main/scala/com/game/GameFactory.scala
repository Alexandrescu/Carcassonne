package com.game

import com.board.{SectionKeeper, StandardLogic, GameBoard}
import com.client.{RealClient, AiClient}
import com.player.Player
import com.server.json.{RoomDetails}

import scala.collection.JavaConversions._

import scala.collection.mutable.ArrayBuffer

object GameFactory {
  def standardGame(roomDetails: RoomDetails) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new StandardTileBag, roomDetails)

  def testGame(roomDetails: RoomDetails) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new TestTileBag, roomDetails)

  implicit def roomDetailsToPlayerTurn(roomDetails: RoomDetails) : PlayerTurns = {
    val playerList = ArrayBuffer[Player]()
    for(slot <- roomDetails.slots.toList) {
      if(!slot.isEmpty) {
        val client = if(slot.isAI) new AiClient else new RealClient
        val player = new Player(client)
        playerList += player
      }
    }
    playerList.sortBy(x => x.client.slot)
    new PlayerTurns(playerList.toArray)
  }
}
