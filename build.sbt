import sbt.Keys.libraryDependencies

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

val zioVersion = "2.0.20"

lazy val root = (project in file("."))
  .settings(
    name := "streams",

    libraryDependencies ++= Seq (
        "dev.zio" %% "zio"         % zioVersion,
        "dev.zio" %% "zio-streams" % zioVersion
    ),
  )
