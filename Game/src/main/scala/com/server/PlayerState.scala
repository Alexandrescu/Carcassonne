package com.server

import com.game.Player
import com.server.json.{GameEnd, RoomDetails}

import scala.collection.JavaConversions._

class PlayerState(room : RoomDetails) {
  var slotMap = Map[Int, Player]()

  for(slot <- room.slots.toList) {
    if(!slot.isAI) {
      slotMap += (slot.slot -> new Player(slot.slot ,slot.playerName, slot.uuid, "token" + slot.slot))
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

  def getPlayer(slot : Int): Unit = {

  }

  def currentSlot() : Int = ???

  def doneConnecting : Boolean = (slotMap.size - connected) == 0

  def nextPlayer : Player = ???

  def isCurrentPlayer(id : String) : Boolean = ???

  def endGame : GameEnd = ???
}
