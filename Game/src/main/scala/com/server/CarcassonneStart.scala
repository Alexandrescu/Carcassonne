package com.server

object CarcassonneStart {
  def main(args: Array[String]): Unit = {
    val server = new ServerScala
    //val server = new TestServer
    try {
      server.start
      //new Server()

      var stopIntercept = false
      while(!stopIntercept) {
        val command = System.in.read
        command match {
          case 'c' =>
            println(Console.GREEN + "Stopping the game." + Console.RESET)
            stopIntercept = true
          case 'm' =>
            println(Console.BLUE + "Logging possible moves: ")
            server.logCurrentMove()
          case 'l' =>
            println(Console.BLUE + "Logging game: ")
            server.logGames()
          case _ =>
        }
      }
    }
    catch {
      case e: Throwable => {
        println(Console.RED + "Caught Exception " + e.toString + Console.RESET)
      }
      case e: java.lang.Exception => {
        println(Console.RED + "Other exception " + e.toString + Console.RESET)
      }
    }

    server.stop
  }
}
