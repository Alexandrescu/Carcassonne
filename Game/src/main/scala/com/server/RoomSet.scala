package com.server

class RoomSet {
  private class Room() {
    private class Player(name : String, var color : String)
    private var playerSet : Map[String, Player] = Map()

    def addPlayer(id : String, player : String, color : String) {
      if(!(playerSet contains id)) {
        playerSet += (id -> new Player(player, color))
      }
    }

    def count = playerSet.size

    def removePlayer(id: String): Unit = {
      playerSet -= id
    }
  }

  private var rooms : Map[String, Room] = Map()

  def getPlayerCount(room: String) : Int = {
    rooms.get(room) match {
      case Some(r) => r.count
      case None => 0
    }
  }

  def addPlayer(room : String, id : String, player : String, color : String) : Unit = {
    rooms.get(room) match {
      case Some(r) => r.addPlayer(id, player, color)
      case None => {}
    }
  }

  def removePlayer(id : String, room : String) = {
    rooms.get(room) match {
      case Some(r) => {
        r.removePlayer(id)
        if(r.count == 0) {
          rooms -= room
        }
      }
      case None => {}
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
