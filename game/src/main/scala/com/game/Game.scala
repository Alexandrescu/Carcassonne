package com.game

import com.corundumstudio.socketio.listener.ConnectListener
import com.corundumstudio.socketio.{SocketIOClient, Configuration, SocketIOServer}

class Game {
  val config = new Configuration()
  config.setHostname("localhost")
  config.setPort(1337)

  val server : SocketIOServer = new SocketIOServer(config)

  server.addConnectListener(new ConnectListener {
    override def onConnect(client: SocketIOClient): Unit = {
      println("A player has connected to the server")
    }
  })
}
