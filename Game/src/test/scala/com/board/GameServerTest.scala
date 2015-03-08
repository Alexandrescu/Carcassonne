package com.board

import java.util.concurrent.TimeoutException

import com.board.mocks.MockPlayer
import com.game.Game
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.{AsyncAssertions, ScalaFutures}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers, ParallelTestExecution}

import scala.concurrent.Await
import scala.concurrent.duration._

class GameServerTest extends FlatSpec with MockFactory with BeforeAndAfter
  with Matchers with ScalaFutures with ParallelTestExecution with AsyncAssertions {

  val mockGame = new Game
  val mockPlayer = new MockPlayer
  before {
    println("Doing the before")
    mockGame.server.start()
  }

  after {
    mockPlayer.disconnect()
    mockGame.server.stop()
  }

  "com.game.Game" should "create a server to which player can connect" in {
    println("How we are waiting for the promise to test2")
    mockPlayer.connect()
    intercept[TimeoutException] {
      Await.ready(mockPlayer.promise.future, 2 seconds)
    }
    assert(mockPlayer.isConnected)
  }

}
