import Dependencies._
import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val muRPCVersion = "0.18.4"
lazy val catsVersion = "1.1.0-M1"

lazy val commonSettings = Seq(addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.patch))

lazy val root = project
  .in(file("."))
  .aggregate(common, server)

lazy val common = (project in file("common"))
  .settings(
    name := "mu-test-common",
    sourceGenerators in Compile += (srcGen in Compile).taskValue,
    idlType := "proto",
    libraryDependencies ++= Seq(
      "io.higherkindness" %% "mu-rpc-channel" % muRPCVersion,
      "io.higherkindness" %% "mu-rpc-fs2" % muRPCVersion
    ),
    commonSettings
  )

lazy val server = (project in file("server"))
  .dependsOn(common)
  .settings(
    name := "mu-test-server",
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
    commonSettings
  )
