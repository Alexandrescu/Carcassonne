package com.server

import com.corundumstudio.socketio._
import com.corundumstudio.socketio.listener.DataListener
import com.game.GameFactory
import com.server.events.{GameEvents, RoomEvents}
import com.server.json.{Room, RoomList}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class ServerScala {
  val logger = Logger(LoggerFactory.getLogger("Carcassonne Server"))

  val config = new Configuration()
  //config.setHostname("localhost")
  config.setPort(1337)

  val server : SocketIOServer = new SocketIOServer(config)

  def start() = server.start()
  def stop() = server.stop()

  val rooms = new RoomSet()

  server.addListeners(new RoomEvents(rooms))

  server.addEventListener("startGame", classOf[Room], new DataListener[Room]() {
    override def onData(client: SocketIOClient, data: Room, ackRequest: AckRequest): Unit = {
      val namespace = server.addNamespace('/' + data.roomName)
      logger.info(s"Starting a new game: ${data.roomName}")
      namespace.addListeners(new GameEvents(GameFactory.standardGame(rooms.getRoomDetails(data.roomName), namespace)))
      availableGames(client)
    }
  })

  server.addEventListener("stopGame", classOf[Room], new DataListener[Room]() {
    override def onData(client: SocketIOClient, data: Room, ackRequest: AckRequest): Unit = {
      logger.info(s"Stopping the game ${data.roomName}")
      server.removeNamespace(data.roomName)
      availableGames(client)
    }
  })

  server.addEventListener("getGame", classOf[Object], new DataListener[Object]() {
    override def onData(client: SocketIOClient, data: Object, ackRequest: AckRequest): Unit = {
      availableGames(client)
    }
  })

  def availableGames(client : SocketIOClient): Unit = {
    val roomList = new RoomList
    roomList.setTheRooms((for(space <- server.getAllNamespaces) yield new Room(space.getName)).toList.toList)
    client.getNamespace.getBroadcastOperations.sendEvent("availableGames", roomList)
  }

}
