import sbt._
import sbt.Keys._

object Build extends Build {
  // factor out common settings into a sequence
  lazy val commonSettings = Seq(
    organization := "com.chunlianglyu.sorm2",
    version := "0.4.3",
    // set the Scala version used for the project
    scalaVersion := "2.11.7"
  )

  lazy val root = Project(
    id = "sorm",
    base = file("."),
    settings = commonSettings ++ Seq(
      // set the name of the project
      name := "sorm2",

      // append -deprecation to the options passed to the Scala compiler
      scalacOptions += "-deprecation",

      publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/Projects/repo.chunlianglyu.com"))),

      // Exclude transitive dependencies, e.g., include log4j without including logging via jdmk, jmx, or jms.
      libraryDependencies ++= Seq(
        "com.mchange" % "c3p0" % "0.9.2-pre5",
        "org.slf4j" % "slf4j-api" % "1.7.12",
        "org.slf4j" % "slf4j-simple" % "1.7.2" % "optional",
        "org.json4s" % "json4s-jackson_2.11" % "3.3.0.RC5",
        "postgresql" % "postgresql" % "9.1-901.jdbc4" % "test",
        "org.hsqldb" % "hsqldb" % "2.2.8" % "test",
        "com.h2database" % "h2" % "1.3.168" % "test",
        "mysql" % "mysql-connector-java" % "5.1.19" % "test",
        "org.scalatest" % "scalatest_2.11" % "2.2.3" % "test",
        "junit" % "junit" % "4.7" % "test",
        "org.scala-lang" % "scala-reflect" % "2.11.7",
        "org.scala-lang" % "scala-library" % "2.11.7"
      )
    )
  )
}