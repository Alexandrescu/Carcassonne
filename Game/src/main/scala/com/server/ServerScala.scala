package com.server

import com.corundumstudio.socketio._
import com.corundumstudio.socketio.annotation._
import com.corundumstudio.socketio.listener.ConnectListener
import com.server.json.Room

class ServerScala {

  val config = new Configuration()
  config.setHostname("localhost")
  config.setPort(1337)

  val server : SocketIOServer = new SocketIOServer(config)

  def start = server.start()
  def stop = server.stop()

  server.addConnectListener(new ConnectListener {
    override def onConnect(client: SocketIOClient): Unit = {

      def getNamespace(socketIONamespace: SocketIONamespace) : String = {
        socketIONamespace.getName
      }

      //val data = new serverTypes.Rooms(server.getAllNamespaces().toList.map(getNamespace))
      //client.sendEvent("availableRooms", data)
    }
  })

  /*server.addEventListener("addRoom", classOf[serverTypes.Room], new DataListener[serverTypes.Room] {
    override def onData(socketIOClient: SocketIOClient, t: serverTypes.Room, ackRequest: AckRequest): Unit = ???
  })
  */

  class ServerEvents {
    @OnEvent("addRoom")
    def onSomeEventHandler(client: SocketIOClient, data: Room) {
      System.out.println("Adding new room with name " + data.roomName)
    }
  }
  server.addListeners(new ServerEvents)

  def addRoom(roomName : String): Unit = {
    server.addNamespace(roomName)
  }

}
