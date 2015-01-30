autoScalaLibrary := false

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.5"
)

lazy val carcassonne = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "Carcassonne",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1"
  )

lazy val game = (project in file("./game/.")).
  settings(commonSettings: _*).
  settings(name := "game").
  dependsOn(carcassonne % "compile->compile;test->test")
