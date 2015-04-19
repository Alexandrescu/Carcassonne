package com.server

object CarcassonneStart {
  def main(args: Array[String]): Unit = {
    val server = new ServerScala
    try {
      server.start
      server.addRoom("test")
      while (System.in.read != 'c') {
        println(Console.BLUE + "[Server] Please press 'c' to stop" + Console.RESET)
      }
    }
    catch {
      case e: Throwable => {
        println(Console.RED + "Caught Exception" + e.toString + Console.RESET)
      }
      case e: java.lang.Exception => {
        println(Console.RED + "Other exception" + e.toString + Console.RESET)
      }
    }

    server.stop
  }
}
