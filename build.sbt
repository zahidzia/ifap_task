name := """world-cities"""
organization := "com.ifap"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.nrinaudo" %% "kantan.csv" % "0.6.1"
libraryDependencies += "com.nrinaudo" %% "kantan.csv-generic" % "0.6.1"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ifap.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ifap.binders._"
