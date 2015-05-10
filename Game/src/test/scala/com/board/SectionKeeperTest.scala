package com.board

import com.player.{Follower, Player, PlayerObserver}
import com.tile.{CitySection, GrassSection}
import org.scalatest.{FlatSpec, PrivateMethodTester}

class SectionKeeperTest extends FlatSpec with PrivateMethodTester {
  val keeper = new SectionKeeper
  trait Collection {
    val observer = new PlayerObserver {
      override def followerUpdate(follower: Follower): Unit = {

      }
    }
    val playerA = new Player(0)
    val playerB = new Player(1)
  }

  "Section keeper" should "union 2 grass sections" in new Collection{
    val grassA = new GrassSection(1)
    val grassB = new GrassSection(2)

    playerA.placeFollower(grassA, (0,0))
    playerA.placeFollower(grassB, (0,0))

    keeper.union(grassA, Set(grassB))

    val rootA = grassA.findRoot()
    val rootB = grassB.findRoot()

    assert(rootA == rootB)

    val cityA = new CitySection(1)
    cityA.addGrass(Set(grassB))
    cityA.valueGrass()

    rootA.closeAtEnd()
    rootB.closeAtEnd()

    assert(rootA.value == 1 && rootB.value == 1)
  }

  it should "update the number of followers" in new Collection {

  }
}
