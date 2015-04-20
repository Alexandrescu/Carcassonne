package com.server

import com.game.Player
import com.server.json.{GameEnd, RoomDetails}

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

class PlayerState(room : RoomDetails) {
  var slotMap = Map[Int, Player]()
  val playerList = ArrayBuffer[Int]()

  for(slot <- room.slots.toList) {
    println(slot)
    if(!slot.isEmpty) {
      if(!slot.isAI) {
        slotMap += (slot.slot -> new Player(slot.slot ,slot.playerName, slot.uuid, "token" + slot.slot))
        playerList += slot.slot
      }
    }
  }

  def updateUUID(slot : Int, token : String, newUUID : String): Unit = {
    slotMap.get(slot) match {
      case Some (player) if player.token == token =>
        player.uuid = newUUID
        connected += 1
      case None => throw new Exception("Slot or token is not valid")
    }
  }

  private var connected = 0
  private var currentSlot = 0

  def doneConnecting : Boolean = {
    (playerList.size - connected) == 0
  }

  def nextPlayer : Player = {
    val result = slotMap.get(playerList(currentSlot)).get
    currentSlot = (currentSlot + 1) % playerList.length
    result
  }

  def isCurrentPlayer(id : String) : Boolean = {
    slotMap.get(playerList(currentSlot)).get.uuid == id
  }

  def endGame : GameEnd = ???
}
