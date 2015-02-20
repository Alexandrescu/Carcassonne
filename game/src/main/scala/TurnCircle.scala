package main.scala

import scala.reflect.ClassTag

/**
 * Created by Andrei on 28/01/15.
 */

class TurnCircle[T] private (val size : Integer, var items : Array[T]){
  def this(size : Integer)(implicit m: ClassTag[T]) = this(size, new Array[T](size))

  private var filled = 0

  def add(item : T): Unit = {
    items(filled) = item
    filled += 1
  }

}