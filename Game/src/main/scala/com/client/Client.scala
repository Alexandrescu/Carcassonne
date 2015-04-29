package com.client

import com.board.{RemovedFollower, Move, PossibleMove}
import com.corundumstudio.socketio.SocketIOClient
import com.player.Player
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

trait Client {
  def slot : Int
  def socketClient : SocketIOClient
  def socketClient_=(socketIOClient: SocketIOClient)

  def token : String
  def name : String
  
  def turn(tile : Tile, moves : Set[PossibleMove]) : Unit
  def currentState(moves: ArrayBuffer[Either[Move, RemovedFollower]]) : Unit
  def player : Player

  var connected : Boolean = false

  def movePlayed(move : Either[Move, RemovedFollower])
}
