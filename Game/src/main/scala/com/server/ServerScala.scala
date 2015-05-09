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
  config.setPort(1337)

  val server : SocketIOServer = new SocketIOServer(config)

  def start() = server.start()
  def stop() = server.stop()

  var gameSet : Set[GameEvents] = Set()
  val rooms = new RoomSet()

  server.addListeners(new RoomEvents(rooms))

  server.addEventListener("startGame", classOf[Room], new DataListener[Room]() {
    override def onData(client: SocketIOClient, data: Room, ackRequest: AckRequest): Unit = {
      if(gameSet.forall(game => game.name != ('/' + data.roomName))) {
        val namespace = server.addNamespace('/' + data.roomName)
        logger.info(s"Starting a new game: ${data.roomName}")

        val game = GameFactory.standardGame(rooms.getRoomDetails(data.roomName), namespace, '/' + data.roomName)
        val gameEvent = game.gameEvents
        gameSet += gameEvent

        namespace.addListeners(gameEvent)
        availableGames(client)
      }
    }
  })

  server.addEventListener("stopGame", classOf[Room], new DataListener[Room]() {
    override def onData(client: SocketIOClient, data: Room, ackRequest: AckRequest): Unit = {
      logger.info(s"Stopping the game ${data.roomName}")
      server.removeNamespace(data.roomName)
      availableGames(client)

      for(game <- gameSet) {
        if(game.name == data.roomName) {
          gameSet -= game
        }
      }
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

  def logGames(): Unit = {
    for(game <- gameSet) {
      println(s"Logging the game: ${game.name}")
      for(move <- game.game.moveList) {
        println(s"$move")
      }
    }
  }

  def logCurrentMove(): Unit = {
    for(game <- gameSet) {
      println(s"Next player on game: ${game.name} is at slot ${game.game.currentClient.slot}")
      println(s"His next tile ${game.game.currentTile.identifier} and possible moves are: ")
      for(move <- game.game.getCurrentMoves) {
        println(move)
      }
    }
  }

  def logPlayers(): Unit = {
    for(game <- gameSet) {
      println(s"Players in the game: ${game.name}")

      for(client <- game.game.getClients) {
        println(s"Client ${client.name} @ ${client.slot} has" +
          s"${client.player.points} points and ${client.player.followers} followers")
      }
    }
  }
}
