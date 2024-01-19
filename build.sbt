val scala3Version = "3.3.1"
val zioVersion = "2.0.19"
val zioJsonVersion = "0.6.2"
val zioHttpVersion = "3.0.0-RC2"
val scalaCsvVersion = "1.3.10"
val zioQuill = "4.7.0"
val zioQuillJdbc = "4.7.0"
val h2Database = "2.2.224"

ThisBuild / organization := "fr.efrei"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := scala3Version


lazy val root = project
  .in(file("."))
  .settings(
      name := "scala3-zio-streams",
      libraryDependencies ++= Seq(
          "dev.zio"       %% "zio"            % zioVersion,
          "dev.zio"       %% "zio-streams"    % zioVersion,
          "dev.zio"       %% "zio-http"       % zioHttpVersion,
          "dev.zio"       %% "zio-json"       % zioJsonVersion,
          "io.getquill"   %% "quill-zio"      % zioQuill,
          "io.getquill"   %% "quill-jdbc-zio" % zioQuillJdbc,
          "com.h2database" % "h2"             % h2Database,
          "io.d11"        %% "zhttp"          % "2.0.0-RC10",
          "dev.zio"       %% "zio-logging"       % "2.1.15",
          "dev.zio"       %% "zio-logging-slf4j" % "2.1.15",
          "org.slf4j"      % "slf4j-simple"      % "2.0.9",
          "mysql" % "mysql-connector-java" % "8.0.23",
          "org.scalatest" %% "scalatest" % "3.2.9" % Test,
          // "org.scalatestplus.zio" %% "scalatestplus-zio" % "1.0.+",
          "dev.zio" %% "zio-test" % "2.0.15" % Test,

          "dev.zio" %% "zio-test-sbt" % "2.0.13" % Test,

      )
  )