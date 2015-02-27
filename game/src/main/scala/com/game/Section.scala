package com.game

abstract class Section
case class TileSection(id : Int) extends Section
case class BoardSection(id : Int) extends Section