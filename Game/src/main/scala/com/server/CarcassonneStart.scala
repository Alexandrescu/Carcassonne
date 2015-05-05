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
            println(Console.RED + "Stopping the game." + Console.RESET)
            stopIntercept = true
          case 'm' =>
            println(Console.BLUE + "Logging possible moves: ")
            server.logCurrentMove()
            println(Console.RESET)
          case 'l' =>
            println(Console.YELLOW + "Logging game: ")
            server.logGames()
            println(Console.RESET)
          case 'p' =>
            println(Console.GREEN + "Player information: ")
            server.logPlayers()
            println(Console.RESET)
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
