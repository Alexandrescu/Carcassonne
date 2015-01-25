name := "Carcassonne"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "2.0" % "test"

lazy val Game = (project in file("./Game/."))