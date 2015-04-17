package com.game

import com.github.nkzawa.emitter.Emitter.Listener
import com.github.nkzawa.socketio.client.IO.Options
import com.github.nkzawa.socketio.client.{IO, Socket}
import org.slf4j.LoggerFactory

import scala.concurrent.Promise

class Player(var name : String, var uuid : String, private var _token : String) {

  def token : String = _token

  private var _followers : Int = 7
  def addFollowers(i: Int): Unit= {
    _followers += i
  }
  def removeFollowers(i : Int = 1) = {
    _followers -= i
  }

  private var _points : Int = 0
  def addPoints(i: Int): Unit = {
    _points += i
  }

  def points : Int = _points

  def hasFollower : Boolean = _followers != 0
  def promise = Promise[Boolean]

  private val logger = LoggerFactory.getLogger(this.getClass)

  private val options : IO.Options = new Options()
  options.forceNew = true
  options.reconnectionDelay = 2000
  options.reconnection = true

  private val socket: Socket = IO.socket("http://localhost:1337", options)

  socket.on(Socket.EVENT_CONNECT, new Listener {
    override def call(args: AnyRef*): Unit = {
      logger.info("%s has connected to the game", name)
      promise success true
    }
  })

  socket.on(Socket.EVENT_CONNECT_ERROR, new Listener {
    override def call(args: AnyRef*): Unit = {
      println(name + " encountered errors when connecting to socket")
    }
  })

  def connect(): Unit = {
    socket.connect()
  }

  def isConnected: Boolean = {
    socket.connected()
  }

  def disconnect(): Unit = {
    socket.disconnect()
  }

}
