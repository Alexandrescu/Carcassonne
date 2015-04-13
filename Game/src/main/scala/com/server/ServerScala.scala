package com.server

import com.corundumstudio.socketio._
import com.corundumstudio.socketio.annotation._
import com.server.json.{Room, Rooms}
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

  var rooms =  Map[String, Int]()

  class ServerEvents {
    @OnConnect
    def onConnect(client: SocketIOClient): Unit = {
      logger.info("Another client has connected ")
      client.sendEvent("availableRooms", getRooms())
    }

    @OnDisconnect
    def onDisconnect(client: SocketIOClient): Unit ={
      logger.info("Player is being disconnected")
      for(room <- client.getAllRooms) {
        logger.info(s"Player was connected to $room.")
        val players = rooms.getOrElse(room, 0)
        if(players > 1) {
          rooms += (room -> (players - 1))
        }
        else {
          rooms -= room
          logger.info(s"${Console.BLUE}[Room]${Console.RESET} Removing $room from rooms")
        }
      }
      availableRooms()
    }

    @OnEvent("addRoom")
    def onAddRoom(client: SocketIOClient, data: Room) : Unit = {
      logger.info(s"Request to create room ${data.getRoom}.")

      if(rooms.contains(data.getRoom)) {
        logger.info("Room not created. Already exists.")
        return
      }
      rooms += (data.getRoom -> 1)

      client.joinRoom(data.getRoom)
      client.sendEvent("roomConnected", data)

      availableRooms()

      logger.info(s"Room ${data.getRoom} created. Broadcast sent.")
    }

    @OnEvent("joinRoom")
    def onJoinRoom(client: SocketIOClient, data : Room) : Unit = {
      logger.info(s"Request to join room ${data.getRoom}.")

      val players = rooms.getOrElse(data.getRoom, 0)
      if(players > 0) {
        rooms -= data.getRoom
        rooms += (data.getRoom -> (players + 1))

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
      def toRoom(roomPair:(String, Int)) : Room = {
        val room = new Room()
        room.setRoom(roomPair._1)
        room
      }

      val data = new Rooms()
      data.setTheName(rooms.map(toRoom).toList)
      data
    }

  }
  server.addListeners(new ServerEvents)

  def addRoom(roomName : String): Unit = {
    server.addNamespace(roomName)
  }

}
