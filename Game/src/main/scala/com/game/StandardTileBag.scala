/*
 * While writing the pieces, I noticed we can actually deduce the grass property, meaning we could be able to simplify
 * the pieces by not having the Road Edge containing all the data.
 *
 * Yet again, we will need to compute the values at running time, meaning this implementation might be taken into account
 * when implementing the AI for efficiency
 */

package com.game

import com.tile._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class StandardTileBag extends TileBag {
  def remove(tile: Tile) = {
    tiles -= tile
    if(thisTile == tile) {
      next()
    }
  }

  private var tiles : ArrayBuffer[Tile] = new ArrayBuffer()
  tiles ++= (for(i <- 1 to 2) yield TileGenerator.A)
  tiles ++= (for(i <- 1 to 4) yield TileGenerator.B)
  tiles ++= (for(i <- 1 to 1) yield TileGenerator.C)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.D)
  tiles ++= (for(i <- 1 to 5) yield TileGenerator.E)
  tiles ++= (for(i <- 1 to 2) yield TileGenerator.F)

  tiles ++= (for(i <- 1 to 1) yield TileGenerator.G)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.H)
  tiles ++= (for(i <- 1 to 2) yield TileGenerator.I)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.J)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.K)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.L)

  tiles ++= (for(i <- 1 to 2) yield TileGenerator.M)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.N)
  tiles ++= (for(i <- 1 to 2) yield TileGenerator.O)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.P)
  tiles ++= (for(i <- 1 to 1) yield TileGenerator.Q)
  tiles ++= (for(i <- 1 to 3) yield TileGenerator.R)

  tiles ++= (for(i <- 1 to 2) yield TileGenerator.S)
  tiles ++= (for(i <- 1 to 1) yield TileGenerator.T)
  tiles ++= (for(i <- 1 to 8) yield TileGenerator.U)
  tiles ++= (for(i <- 1 to 9) yield TileGenerator.V)
  tiles ++= (for(i <- 1 to 4) yield TileGenerator.W)
  tiles ++= (for(i <- 1 to 1) yield TileGenerator.X)

  private var sections : Set[Section] = Set()

  for(tile <- tiles) {
    sections ++= tile.getSections()
  }

  override val startTile: Tile = TileGenerator.D

  override def getEntireTileBag: Set[Tile] = tiles.toSet

  override def hasNext: Boolean = tiles.size > 0

  override def allSections: Set[Section] = sections

  private var thisTile : Tile = startTile

  override def next(): Tile = {
    val random = Random.nextInt(tiles.length)
    thisTile = tiles.remove(random)
    thisTile
  }

  override def current: Tile = thisTile

  override def getTile(name: String): Tile = {
    /* Can do binary search */
    for(i <- 0 until tiles.size) {
      if(tiles(i).identifier == name) {
        return tiles(i)
      }
    }
    /* Should throw error here or tile might be this */
    if(thisTile.identifier == name) {
      return thisTile
    }

    null
  }
}
