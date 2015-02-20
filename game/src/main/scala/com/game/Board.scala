package com.game

import main.scala.Tile

trait Board {
  final type Place = (Integer, Integer)
  def get(place : Place) : Option[Tile]
  def getBoard() : Map[Place, Tile]
  def setMove(tile: Tile, place : Place, tileSection : Section, follower : PlayerFollower) : Boolean
  def isMove(tile : Tile, place : Place, tileSection : Section, follower : PlayerFollower) : Boolean
}
