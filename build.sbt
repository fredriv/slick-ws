val slick = "com.typesafe.slick" %% "slick" % "3.1.1"
val h2db = "com.h2database" % "h2" % "1.4.191"
val scalatest = "org.scalatest" %% "scalatest" % "2.2.6" % "test"

lazy val root = (project in file(".")).
  settings(
    name := "slick-ws",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(slick, h2db, scalatest)
  )