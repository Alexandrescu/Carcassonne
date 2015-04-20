package com.server.events

import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.annotation.{OnConnect, OnDisconnect, OnEvent}
import com.server.RoomSet
import com.server.json.{Room, RoomDetails, RoomJoin, RoomList}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class RoomEvents(val rooms: RoomSet, val name : String = "Server") {
  val logger : Logger = Logger(LoggerFactory.getLogger(name))
  @OnConnect
  def onConnect(client: SocketIOClient): Unit = {
    logger.info(s"${client.getNamespace.getName}")
    logger.info(s"Another client has connected ${client.getSessionId.toString}")
    client.sendEvent("availableRooms", getRooms())
  }

  @OnDisconnect
  def onDisconnect(client: SocketIOClient): Unit ={
    logger.info(s"Player is being disconnected ${client.getSessionId.toString}")
    for(room <- client.getAllRooms) {
      logger.info(s"Removing player from $room.")
      val newRoom : RoomDetails = rooms.removeClient(room, client.getSessionId.toString)
      client.getNamespace.getRoomOperations(room).sendEvent("roomUpdate", newRoom)
    }
    availableRooms(client)
  }

  @OnEvent("addRoom")
  def onAddRoom(client: SocketIOClient, data: Room) : Unit = {
    logger.info(s"Request to create room ${data.roomName}.")

    if(rooms.contains(data.roomName)) {
      logger.info("Room not created. Already exists.")
      return
    }

    val newRoom : RoomDetails = rooms.addRoom(data.roomName)
    rooms.addClient(data.roomName, client.getSessionId.toString)

    client.joinRoom(data.roomName)
    client.sendEvent("roomConnected", newRoom)

    availableRooms(client)

    logger.info(s"Room ${data.roomName} created. Broadcast sent.")
  }

  @OnEvent("joinRoom")
  def onJoinRoom(client: SocketIOClient, data : Room) : Unit = {
    logger.info(s"Request to join room ${data.roomName}.")

    if(rooms.contains(data.roomName)) {
      val roomDetails : RoomDetails = rooms.addClient(data.roomName, client.getSessionId.toString)

      client.joinRoom(data.roomName)
      client.sendEvent("roomConnected", roomDetails)
    }
    else {
      logger.warn("Attempting to connect to unexisting room.")
    }
  }

  @OnEvent("leaveRoom")
  def onLeaveRoom(client: SocketIOClient, data : Room) : Unit = {
    logger.info(s"Request to leave room ${data.roomName}")
    if(rooms.contains(data.roomName)) {
      val newRoom : RoomDetails = rooms.removeClient(data.roomName, client.getSessionId.toString)
      client.getNamespace.getRoomOperations(data.roomName).sendEvent("roomUpdate", newRoom)
      client.leaveRoom(data.roomName)
      client.sendEvent("roomLeft", data)
    }
    availableRooms(client)
  }

  @OnEvent("addPlayer")
  def onAddPlayer(client : SocketIOClient, data : RoomJoin) : Unit = {
    logger.info(s"Request to add player to room ${data.roomName}")
    if(rooms.contains(data.roomName)) {
      val newRoom : RoomDetails = rooms.addPlayer(data.roomName, client.getSessionId.toString, data.playerName, data.slot, data.isAI)
      client.getNamespace.getRoomOperations(data.roomName).sendEvent("roomUpdate", newRoom)
    }
  }

  @OnEvent("removePlayer")
  def onRemovePlayer(client: SocketIOClient, data: RoomJoin) : Unit = {
    logger.info(s"Request to empty a slot ${data.slot}")
    if(rooms.contains(data.roomName)) {
      val newRoom : RoomDetails = rooms.removePlayer(data.roomName, data.slot)
      client.getNamespace.getRoomOperations(data.roomName).sendEvent("roomUpdate", newRoom)
    }
  }

  def availableRooms(client : SocketIOClient): Unit = {
    client.getNamespace.getBroadcastOperations.sendEvent("availableRooms", getRooms())
  }

  def getRooms(): RoomList = {
    val data = new RoomList()
    data.setTheRooms(rooms.getRooms().map(new Room(_)).toList)
    data
  }

}
