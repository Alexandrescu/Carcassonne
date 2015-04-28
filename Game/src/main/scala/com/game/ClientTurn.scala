package com.game

import com.client.Client
import com.corundumstudio.socketio.SocketIOClient
import com.server.json.GameClient
import com.tile.Tile

class ClientTurn(clients : Array[Client]) {
  var nextClient = 0
  var client : Client = clients(nextClient)

  val gameSize = clients.length

  def updateClient(socketClient: SocketIOClient, info: GameClient): Unit = {
    for(client <- clients) {
      if(info.slot == client.slot && info.token == client.token) {
        client.socketClient = socketClient
      }
    }
  }

  def current: Client = client

  /*
      This is going to tell the next client to move, update the current and nextClient
   */

  def next(tile : Tile): Unit = {
    client = clients(nextClient)
    client.turn(tile)

    nextClient = (nextClient + 1) % gameSize
  }
}
