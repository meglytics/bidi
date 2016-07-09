import sbt._

name := "meglyticsgw"

version := "0.2"

scalaVersion := "2.11.7"

organization := "Megam Systems"

homepage := Some(url("https://www.megam.io"))

description := """Meglytics Gateway : Scalable RESTful API server for meglytics
                  in a functional way, built using ScyllDB
                  try: http://www.meglytics.com"""


javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

javaOptions ++= Seq("-Dconfig.file=" + {
  val home  = System getenv "MEGAM_HOME"
  if (home == null || home.length <=0) sys.error("Must define MEGAM_HOME")
  val gwconfPath = Path(home)
  val gwconf = gwconfPath / "megamgateway" /  "gateway.conf"
  gwconf.toString
},
"-Dlogger.file=" + {
  val home  = System getenv "MEGAM_HOME"
  if (home == null || home.length <=0) sys.error("Must define MEGAM_HOME")
  val logconfPath = Path(home)
  val logconf = logconfPath / "megamgateway" /  "logger.xml"
  logconf.toString
})

scalacOptions := Seq(
  "-deprecation",
  "-feature",
  "-optimise",
  "-Xcheckinit",
  "-Xlint",
  "-Xverify",
  "-Yinline",
  "-Yclosure-elim",
  "-Yconst-opt",
  "-Ydelambdafy:method" ,
  "-Ybackend:GenBCode",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:reflectiveCalls",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-Ydead-code")


incOptions := incOptions.value.withNameHashing(true)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
resolvers += "Spray repo" at "http://repo.spray.io"
resolvers += "Spy Repository" at "http://files.couchbase.com/maven2"
resolvers += "Bintray megamsys" at "https://dl.bintray.com/megamsys/scala/"
resolvers += "Websudos" at "https://dl.bintray.com/websudos/oss-releases/"


libraryDependencies ++= Seq(filters, cache,
  "org.yaml" % "snakeyaml" % "1.16",
  "io.megam" %% "libcommon" % "0.26",
  "io.megam" %% "newman" % "1.3.12",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.1",
  "org.specs2" %% "specs2-core" % "3.7-scalaz-7.1.6" % "test",
  "com.websudos"  %% "phantom-dsl" % "1.11.0",
  "org.specs2" % "specs2-matcher-extra_2.11" % "3.7-scalaz-7.1.6" % "test")
