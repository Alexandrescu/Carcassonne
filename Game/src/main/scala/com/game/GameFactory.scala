package com.game

import com.board.{GameBoard, SectionKeeper, StandardLogic}
import com.client.{AiClient, Client, RealClient}
import com.server.json.RoomDetails

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

object GameFactory {
  def standardGame(roomDetails: RoomDetails) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new StandardTileBag, roomDetails)

  def testGame(roomDetails: RoomDetails) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new TestTileBag, roomDetails)

  implicit def roomDetailsToPlayerTurn(roomDetails: RoomDetails) : ClientTurn = {
    val clientList = ArrayBuffer[Client]()
    for(slot <- roomDetails.slots.toList) {
      if(!slot.isEmpty) {
        val client = if(slot.isAI) new AiClient else new RealClient
        clientList += client
      }
    }
    clientList.sortBy(x => x.slot)
    new ClientTurn(clientList.toArray)
  }
}
