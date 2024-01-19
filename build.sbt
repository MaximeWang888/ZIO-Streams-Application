val scala3Version = "3.3.1"
val zioVersion = "2.0.19"
val zioJsonVersion = "0.6.2"
val zioHttpVersion = "3.0.0-RC2"
val scalaCsvVersion = "1.3.10"
val zioQuill = "4.7.0"
val zioQuillJdbc = "4.7.0"
val zioLogging = "2.1.15"
val zhttp = "2.0.0-RC10"
val h2Database = "2.2.224"
val slf4jSimple = "2.0.9"
val circle = "0.14.5"
val mysqlVersion = "8.0.23"

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
          "io.d11"        %% "zhttp"          % zhttp,
          "dev.zio"       %% "zio-logging"       % zioLogging,
          "dev.zio"       %% "zio-logging-slf4j" % zioLogging,
          "org.slf4j"      % "slf4j-simple"      % slf4jSimple,
          "mysql" % "mysql-connector-java" % mysqlVersion,
          "io.circe" %% "circe-core" % circle,
          "io.circe" %% "circe-generic" % circle,
          "io.circe" %% "circe-parser" % circle,
      )
  )