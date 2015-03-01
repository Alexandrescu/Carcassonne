package com.game

import main.scala.{Player, Tile}

trait Board {
  final type Place = (Integer, Integer)
  def get(place : Place) : Option[Tile]
  def getBoard() : Map[Place, Tile]
  def setMove(tile: Tile, place : Place, toOwn : Option[(TileSection, Player)]) : Unit
  def isMove(tile : Tile, place : Place, toOwn : Option[TileSection]) : Boolean
}
