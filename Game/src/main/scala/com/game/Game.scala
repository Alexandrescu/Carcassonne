package com.game

import com.board.{GameBoard, Move, PossibleMove, RemovedFollower}
import com.client.{AiClient, Client, RealClient}
import com.corundumstudio.socketio.{SocketIONamespace, SocketIOClient}
import com.player.{Follower, Player, PlayerObserver}
import com.server.Converter
import com.server.events.GameEvents
import com.server.json.{GameMove, GameClient, GameClientPlayer, GameSlots}
import com.tile.Tile
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

class Game(board : GameBoard, tileBag : TileBag, clientTurn: ClientTurn, namespace: SocketIONamespace, gameName : String) {
  val gameEvents = new GameEvents(this, namespace, gameName)
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
  //Adding games to AiS
  for(client <- clientTurn.clients) client match {
    case ai : AiClient =>
      ai.registerGame(this)
      ai.connected = true
    case _ =>
  }

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
      logger.info("GAME HAS ENDED!")
      return
    }

    playedTile = false
    val gameClient = clientTurn.next()
    var currentTile = tileBag.next()
    currentMoves = board.getMoves(currentTile, gameClient.player)
    while(currentMoves.size == 0 && tileBag.hasNext) {
      currentTile = tileBag.next()
      currentMoves = board.getMoves(currentTile, gameClient.player)
    }

    if(currentMoves.size == 0) {
      playedTile = true
      val endGameResult = summary

      for(client <- clientTurn.clients) {
        client.endGame(endGameResult)
      }

      namespace.getBroadcastOperations
        .sendEvent("gameEnd", new GameSlots(endGameResult.toList))
      return
    }

    // Sending the draw to people
    for(client <- clientTurn.clients) {
      client.draw(currentTile, gameClient.player)
    }
    namespace.getBroadcastOperations.
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
    val result = moveQueue.takeRight(moveQueue.size - startPoint).toArray
    for(move <- result) {
      for(client <- clientTurn.clients) {
        client.movePlayed(move)
      }
    }
    result
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

  private def runGame(): Unit = {
    if(!_started && clientTurn.doneConnecting) {
      logger.info("Starting the game.")
      _started = true
      next()
    }
  }

  /* Getters */
  def getClient(client : SocketIOClient) : Client = {
    for(gameClient <- clientTurn.clients) gameClient match {
      case realClient : RealClient =>
        if(realClient.socketClient.getSessionId == client.getSessionId) {
          return realClient
        }
      case _ =>
    }

    throw new Error("No such client in the game. Please register first.")
  }

  def currentClient : Client = clientTurn.current

  def getClients : Array[Client] = clientTurn.clients

  def isCurrentPlayer(gameClient : Client): Boolean = clientTurn.current == gameClient

  def currentPlayer : Player = clientTurn.current.player

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
    logger.info("GAME ENDING.")
    for(section <- tileBag.allSections) {
      section.closeAtEnd()
    }

    getSlots
  }

  /* Handling Ai moves */
  def aiMove(move : GameMove, aiClient : Client): Unit = {
    if(isCurrentPlayer(aiClient)) {
      gameEvents.runMove(move, aiClient)
    }
  }

  runGame()
}
