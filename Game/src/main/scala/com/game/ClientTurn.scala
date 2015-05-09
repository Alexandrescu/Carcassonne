package com.game

import com.client.{RealClient, Client}
import com.corundumstudio.socketio.SocketIOClient
import com.server.json.GameClient

class ClientTurn(val clients : Array[Client]) {
  var nextClient = 0
  private var client : Client = clients(nextClient)

  val gameSize = clients.length

  def updateClient(socketClient: SocketIOClient, info: GameClient): Client = {
    for(client <- clients) client match {
      case realClient : RealClient => {
        if (info.slot == client.slot && info.token == client.token) {
          realClient.socketClient = socketClient
          client.connected = true
          return client
        }
      }
    }
    throw UninitializedFieldError("No client with this slot and token.")
  }

  def doneConnecting : Boolean = clients forall (_ connected)

  def current: Client = client

  def next(): Client = {
    client = clients(nextClient)
    nextClient = (nextClient + 1) % gameSize
    client
  }
}
