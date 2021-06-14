import sbt._

object Dependencies {
  lazy val scalaTest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.8" % "test,it",
  )

  val circeVersion = "0.13.0"
  val pureconfigVersion = "0.15.0"
  val catsVersion = "2.2.0"
  val lessonServiceVersion = "0.1.0-SNAPSHOT"
  val tapirVersion = "0.14.5"

  lazy val core = Seq(
    // cats FP libary
    "org.typelevel" %% "cats-core" % catsVersion,

    // support for JSON formats
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-literal" % circeVersion,

    // tapir client for consumer based contract testing and integration testing
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion,
    "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % "2.1.1",

    // API specification dependency
    "com.example.lesson-service" %% "api_spec" % lessonServiceVersion,

    // support for typesafe configuration
    "com.github.pureconfig" %% "pureconfig" % pureconfigVersion,

    // logging
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}
