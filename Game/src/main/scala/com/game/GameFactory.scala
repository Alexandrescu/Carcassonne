package com.game

import com.board.{GameBoard, SectionKeeper, StandardLogic}
import com.client.{AiClient, Client, RealClient}
import com.corundumstudio.socketio.SocketIONamespace
import com.server.json.RoomDetails

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

object GameFactory {
  def standardGame(roomDetails: RoomDetails, namespace : SocketIONamespace, gameName : String) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new StandardTileBag, roomDetails, namespace, gameName)

  def testGame(roomDetails: RoomDetails, namespace : SocketIONamespace, gameName : String) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new TestTileBag, roomDetails, namespace, gameName)

  implicit def roomDetailsToPlayerTurn(roomDetails: RoomDetails) : ClientTurn = {
    val gameSize = roomDetails.slots.count(p => !p.isEmpty)
    val clientList = ArrayBuffer[Client]()
    for(slot <- roomDetails.slots.toList) {
      if(!slot.isEmpty) {
        val client =
          if(slot.isAI) new AiClient(slot.slot, slot.slot, slot.token, slot.playerName, gameSize)
          else new RealClient(slot.slot, "token" + slot.slot, slot.playerName)
        clientList += client
      }
    }
    clientList.sortBy(x => x.slot)
    new ClientTurn(clientList.toArray)
  }
}
