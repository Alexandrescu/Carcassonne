package com.game

import java.util.UUID

import com.board.{GameBoard, SectionKeeper, StandardLogic}
import com.client.{AiClient, Client, RealClient}
import com.corundumstudio.socketio.SocketIONamespace
import com.server.json.RoomDetails

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

object GameFactory {
  def standardGame(roomDetails: RoomDetails, socketSpace : SocketIONamespace) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new StandardTileBag,
      roomDetailsToPlayerTurn(roomDetails, socketSpace))

  def testGame(roomDetails: RoomDetails, socketSpace : SocketIONamespace) : Game =
    new Game(new GameBoard(new StandardLogic, new SectionKeeper), new TestTileBag,
             roomDetailsToPlayerTurn(roomDetails, socketSpace))

  def roomDetailsToPlayerTurn(roomDetails: RoomDetails, socketSpace: SocketIONamespace) : ClientTurn = {
    val clientList = ArrayBuffer[Client]()
    for(slot <- roomDetails.slots.toList) {
      if(!slot.isEmpty) {
        val client =
          if(slot.isAI) new AiClient
          else new RealClient(slot.slot, slot.token, slot.playerName, socketSpace.getClient(UUID.fromString(slot.uuid)))
        clientList += client
      }
    }
    clientList.sortBy(x => x.slot)
    new ClientTurn(clientList.toArray)
  }
}
