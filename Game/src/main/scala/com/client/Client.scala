package com.client

import com.board.{PossibleMove, Move}
import com.corundumstudio.socketio.SocketIOClient
import com.player.{Player, PlayerObserver}
import com.tile.Tile

import scala.collection.mutable.ArrayBuffer

trait Client extends PlayerObserver{
  def slot : Int
  def socketClient : SocketIOClient
  def socketClient_=(socketIOClient: SocketIOClient)

  def token : String
  def name : String
  
  def turn(tile : Tile, moves : Set[PossibleMove]) : Unit
  def currentState(moves: ArrayBuffer[Move]) : Unit
  def player : Player

  var connected : Boolean = false
}
