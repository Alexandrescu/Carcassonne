package com.game

import com.board.{GameBoard, Move, PossibleMove, RemovedFollower}
import com.client.Client
import com.corundumstudio.socketio.SocketIOClient
import com.player.{Follower, Player, PlayerObserver}
import com.server.Converter
import com.server.json.{GameSlots, GameClient, GameClientPlayer}
import com.tile.Tile
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

class Game(board : GameBoard, tileBag : TileBag, clientTurn: ClientTurn) {
  type EitherMove = Either[Move, RemovedFollower]

  /* Observing follower moves */
  private val observer = new PlayerObserver {
    override def followerUpdate(follower: Follower): Unit = {
      val newRF = new RemovedFollower(follower.removedPlace, follower.removedFrontEndId, follower.player)
      logger.info(newRF.toString)
      announceClients(Right(newRF))
      moveQueue += Right(newRF)
    }
  }
  clientTurn.clients.foreach(_.player.registerObserver(observer))

  val logger : Logger = Logger(LoggerFactory.getLogger("Game"))
  private var moveQueue : ArrayBuffer[EitherMove] =
    ArrayBuffer(Left(new Move(tileBag.startTile, (0, 0), None, null)))
  board.setMove(moveQueue.head.left.get)

  private var currentMoves : Set[PossibleMove] = Set()
  private var playedTile = false

  /*
      PRE: Game not ended
      POST: Game in the next stage: tile removed, player informed
   */
  def next(): Unit = {
    // MAYBE CHECK IF THE GAME IS DONE
    if(!tileBag.hasNext) {
      println("GAME HAS ENDED")
      return
    }

    playedTile = false
    val gameClient = clientTurn.next()
    var currentTile = tileBag.next()

    currentMoves = board.getMoves(currentTile, gameClient.player)
    while(currentMoves.size == 0 && tileBag.hasNext) {
      currentTile = tileBag.next()
    }

    if(currentMoves.size == 0) {
      playedTile = true
      gameClient.socketClient.getNamespace.getBroadcastOperations
        .sendEvent("gameEnd", new GameSlots(summary.toList))
      return
    }

    // Sending the draw to people
    gameClient.socketClient.getNamespace.getBroadcastOperations.
      sendEvent("gameDraw", Converter.toGameDraw(currentTile, gameClient.player))

    gameClient.turn(currentTile, currentMoves)
  }

  /* State handlers */
  def currentTile : Tile = tileBag.current

  def finished: Boolean = !tileBag.hasNext && playedTile

  def moveList : List[EitherMove] = moveQueue.clone().toList

  def isMove(move : Move): Boolean = {
    board.isMove(move)
  }

  def setMove(move : Move): Array[EitherMove] = {
    logger.info(s"Played move: $move.")

    val startPoint = moveQueue.size

    moveQueue += Left(move)
    announceClients(Left(move))

    board.setMove(move)

    playedTile = true
    moveQueue.takeRight(moveQueue.size - startPoint).toArray
  }

  /* Informing a client when connecting to the game */
  def informClient(gameClient : Client): Unit = {
    gameClient.currentState(moveQueue.clone())
  }

  private var _started = false

  def started : Boolean = _started

  def updateClient(client : SocketIOClient, info : GameClient): Unit = {
    val thisClient = clientTurn.updateClient(client, info)
    if(!_started && clientTurn.doneConnecting) {
      logger.info("Starting the game.")
      _started = true
      next()
    }
    else if(_started && isCurrentPlayer(thisClient)) {
      thisClient.turn(tileBag.current, currentMoves)
    }
  }

  /* Getters */
  def getClient(client : SocketIOClient) : Client = {
    for(gameClient <- clientTurn.clients) {
      if(gameClient.socketClient.getSessionId == client.getSessionId) {
        return gameClient
      }
    }

    throw new Error("No such client in the game. Please register first.")
  }

  def isCurrentPlayer(gameClient : Client): Boolean = clientTurn.current == gameClient

  def currentPlayer : Player = clientTurn.current.player

  def currentClient : Client = clientTurn.current

  private def announceClients(move : EitherMove): Unit = {
    clientTurn.clients.foreach(_.movePlayed(move))
  }

  def getSlots : List[GameClientPlayer] = {
    clientTurn.clients.map(client =>
      new GameClientPlayer(client.slot, client.player.followers, client.player.points, client.name)).toList
  }

  def getCurrentMoves : Set[PossibleMove] = currentMoves


  /* Ending a game */
  def summary : List[GameClientPlayer] = {
    for(section <- tileBag.allSections) {
      section.finishSection()
    }

    getSlots
  }
}
