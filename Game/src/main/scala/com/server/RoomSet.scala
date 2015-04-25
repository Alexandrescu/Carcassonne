package com.server

import com.server.json.{RoomSlot, RoomDetails}

import scala.collection.JavaConversions._

class RoomSet {
  private val playerNumber = 6

  private class Room(val roomName : String) {
    abstract class PlayerType(val name : String)
    case class HumanPlayer(override val name : String, id : String) extends PlayerType(name)
    case class AI(override val name : String) extends PlayerType(name)

    class PlayerSet {
      var playerSet = Map[Int, PlayerType]()

      def addPlayer(slot : Int, playerType: PlayerType): Unit = {
        playerSet += (slot -> playerType)
      }

      def removePlayer(slot : Int): Unit = {
        playerSet -= slot
      }

      def removePlayer(id : String): Unit = {
        for((slot, player) <- playerSet) {
          player match {
            case HumanPlayer(_, playerId) if playerId == id => playerSet -= slot
            case _ =>
          }
        }
      }

      def toJSON() : RoomDetails = {
        val json = new RoomDetails()
        json.roomName = roomName
        json.slots = (for(i <- 0 until playerNumber) yield {
          val newSlot = new RoomSlot()
          newSlot.slot = i
          newSlot.isEmpty = false
          playerSet.get(i) match {
            case Some(HumanPlayer(name, uuid)) =>
              newSlot.playerName = name
              newSlot.isAI = false
              newSlot.uuid = uuid
            case Some(AI(name)) =>
              newSlot.playerName = name
              newSlot.isAI = true
            case None =>
              newSlot.isEmpty = true
          }
          newSlot
        }).toList
        json
      }
    }

    class ClientSet {
      var clientSet = Set[String]()

      def count = clientSet.size

      def addClient(id : String): Unit = {
        clientSet += id
      }

      def removeClient(id : String): Unit = {
        clientSet -= id
      }
    }

    val clientSet = new ClientSet()
    val playerSet = new PlayerSet()

    def addPlayer(slot : Int, playerName : String, id : String): Unit = {
      playerSet.addPlayer(slot, HumanPlayer(playerName, id))
    }

    def removePlayer(slot : Int): Unit = {
      playerSet.removePlayer(slot)
    }

    def addAI(slot : Int, name : String): Unit = {
      playerSet.addPlayer(slot, AI(name))
    }

    def addClient(id : String): Unit = {
      clientSet.addClient(id)
    }

    def removeClient(id : String): Unit = {
      playerSet.removePlayer(id)
      clientSet.removeClient(id)
    }

    def countClients = clientSet.count
    
    def toJSON : RoomDetails = {
      playerSet.toJSON()
    }
  }

  private var rooms : Map[String, Room] = Map()

  def getClientCount(room: String) : Int = {
    rooms.get(room) match {
      case Some(r) => r.countClients
      case None => 0
    }
  }

  def addClient(room : String, id : String): RoomDetails = {
    rooms.get(room) match {
      case Some(r) =>
        r.addClient(id)
        r.toJSON
      case _ => null
    }
  }

  def removeClient(room : String, id : String): RoomDetails = {
    rooms.get(room) match {
      case Some(r) =>
        r.removeClient(id)
        if(r.countClients == 0) {
          rooms -= room
          return null
        }
        r.toJSON
      case _ => throw UninitializedFieldError("Room doesn't exist")
    }
  }

  def addPlayer(room : String, id : String, playerName : String, slot : Int, isAI : Boolean = false) : RoomDetails = {
    val json = rooms.get(room) match {
      case Some(r) if !isAI =>
        r.addPlayer(slot, playerName, id)
        r.toJSON
      case Some(r) if isAI =>
        r.addAI(slot, playerName)
        r.toJSON
      case None => null
    }

    if(json != null) {
      json.roomName = room
    }
    json
  }

  def removePlayer(room : String, slot : Int) : RoomDetails = {
    rooms.get(room) match {
      case Some(r) =>
        r.removePlayer(slot)
        r.toJSON
      case None => null
    }
  }

  def contains(room: String) : Boolean = rooms.contains(room)

  def addRoom(room : String) : RoomDetails = {
    val newRoom = new Room(room)
    rooms += (room -> newRoom)

    newRoom.toJSON
  }

  def getRooms() : List[String] = {
    (for(pair <- rooms) yield pair._1).toList
  }

  def getRoomDetails(room : String) : RoomDetails = {
    rooms.get(room) match {
      case Some(r) => r.toJSON
      case _ => throw UninitializedFieldError("Room doesn't exist")
    }
  }
}
