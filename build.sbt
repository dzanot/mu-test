import Dependencies._
import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val muRPCVersion = "0.18.4"
val catsVersion = "1.1.0-M1"

lazy val root = (project in file("."))
  .settings(
    name := "mu-test",
    libraryDependencies += scalaTest % Test,
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-server" % muRPCVersion,
      "io.higherkindness" %% "mu-rpc-channel" % muRPCVersion,
      "io.higherkindness" %% "mu-rpc-fs2" % muRPCVersion,
      "io.higherkindness" %% "mu-rpc-netty" % muRPCVersion,
      "io.chrisdavenport" %% "log4cats-slf4j"   % "0.4.0-M1",
      "ch.qos.logback"     % "logback-classic"          % "1.2.3",
      "org.typelevel" %% "cats-effect" % catsVersion,
    ),
    idlType := "proto",
    sourceGenerators in Compile += (srcGen in Compile).taskValue,
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch)
  )

