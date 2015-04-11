import sbt.Keys._

autoScalaLibrary := false

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.5"
)

lazy val carcassonne = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Carcassonne",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1",
    libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
    libraryDependencies += "com.github.nkzawa" % "socket.io-client" % "0.4.1",
    libraryDependencies += "com.corundumstudio.socketio" % "netty-socketio" % "1.7.7",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.10",
    libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.10",
    libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.10"
  )

lazy val game = (project in file("./game/.")).
  settings(commonSettings: _*).
  settings(name := "game").
  dependsOn(carcassonne % "compile->compile;test->test")
