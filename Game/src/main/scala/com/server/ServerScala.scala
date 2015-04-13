package com.server

import com.corundumstudio.socketio._
import com.corundumstudio.socketio.annotation._
import com.server.json.{Room, RoomJoin, RoomUpdate, Rooms}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class ServerScala {
  val logger = Logger(LoggerFactory.getLogger("Carcassonne Server"))

  val config = new Configuration()
  config.setHostname("localhost")
  config.setPort(1337)

  val server : SocketIOServer = new SocketIOServer(config)

  def start() = server.start()
  def stop() = server.stop()

  val rooms = new RoomSet()
  class ServerEvents {
    @OnConnect
    def onConnect(client: SocketIOClient): Unit = {
      logger.info(s"Another client has connected ${client.getSessionId.toString}")
      client.sendEvent("availableRooms", getRooms())
    }

    @OnDisconnect
    def onDisconnect(client: SocketIOClient): Unit ={
      logger.info(s"Player is being disconnected ${client.getSessionId.toString}")
      for(room <- client.getAllRooms) {
        logger.info(s"Removing player from $room.")
        rooms.removePlayer(client.getSessionId.toString, room)
      }
      availableRooms()
    }

    @OnEvent("addRoom")
    def onAddRoom(client: SocketIOClient, data: RoomJoin) : Unit = {
      logger.info(s"Request to create room ${data.getRoom}.")

      if(rooms.contains(data.getRoom)) {
        logger.info("Room not created. Already exists.")
        return
      }

      rooms.addRoom(data.getRoom)
      rooms.addPlayer(data.getRoom, client.getSessionId.toString, data.getUsername, data.getUserColor)

      client.joinRoom(data.getRoom)
      client.sendEvent("roomConnected", data)

      availableRooms()

      logger.info(s"Room ${data.getRoom} created. Broadcast sent.")
    }

    @OnEvent("joinRoom")
    def onJoinRoom(client: SocketIOClient, data : RoomJoin) : Unit = {
      logger.info(s"Request to join room ${data.getRoom}.")

      if(rooms.contains(data.getRoom)) {
        rooms.addPlayer(data.getRoom, client.getSessionId.toString, data.getUsername, data.getUserColor)

        client.joinRoom(data.getRoom)
        client.sendEvent("roomConnected", data)
      }
      else {
        logger.warn("Attempting to connect to unexisting room.")
      }
    }

    def availableRooms(): Unit = {
      server.getBroadcastOperations.sendEvent("availableRooms", getRooms())
    }

    def getRooms(): Rooms = {
      val data = new Rooms()
      data.setTheRooms(rooms.getRooms().map(new Room(_)).toList)
      data
    }

  }
  server.addListeners(new ServerEvents)

  def addRoom(roomName : String): Unit = {
    server.addNamespace(roomName)
  }

}
