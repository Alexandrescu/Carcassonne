package com.server

class RoomSet {

  private class Room() {
    abstract class PlayerType
    case class HumanPlayer(name : String, id : String) extends PlayerType
    case class AI() extends PlayerType

    class PlayerSet {
      var playerSet = Map[Int, PlayerType]()

      def addPlayer(slot : Int, playerType: PlayerType): Unit = {
        playerSet += (slot -> playerType)
      }

      def removePlayer(slot : Int): Unit = {
        playerSet -= slot
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

    def addAI(slot : Int): Unit = {
      playerSet.addPlayer(slot, AI())
    }

    def addClient(id : String): Unit = {
      clientSet.addClient(id)
    }

    def removeClient(id : String): Unit = {
      clientSet.removeClient(id)
    }

    def countClients = clientSet.count
  }

  private var rooms : Map[String, Room] = Map()

  def getClientCount(room: String) : Int = {
    rooms.get(room) match {
      case Some(r) => r.countClients
      case None => 0
    }
  }

  def addClient(room : String, id : String): Unit = {
    rooms.get(room) match {
      case Some(r) => r.addClient(id)
      case _ =>
    }
  }

  def removeClient(room : String, id : String): Unit = {
    rooms.get(room) match {
      case Some(r) => {
        r.removeClient(id)
        if(r.countClients == 0) {
          rooms -= room
        }
      }
      case _ =>
    }
  }

  def addPlayer(room : String, id : String, playerName : String, slot : Int, isAI : Boolean = false) : Unit = {
    rooms.get(room) match {
      case Some(r) if !isAI => r.addPlayer(slot, playerName, id)
      case Some(r) if isAI => r.addAI(slot)
      case None => {}
    }
  }

  def removePlayer(room : String, slot : Int) = {
    rooms.get(room) match {
      case Some(r) => r.removePlayer(slot)
      case None =>
    }
  }

  def contains(room: String) : Boolean = rooms.contains(room)

  def addRoom(room : String) : Unit = {
    rooms += (room -> new Room())
  }

  def getRooms() : List[String] = {
    (for(pair <- rooms) yield pair._1).toList
  }
}
