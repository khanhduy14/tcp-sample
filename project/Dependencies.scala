import Dependencies.Exclusions.slf4jExclusions
import sbt._

object Dependencies {

  import Versions._

  object Versions {
    val akkaActorVersion = "2.7.0"
    val slickVersion = "3.4.1"
    val slickPGVersion = "0.21.0"
    val log4jCoreVersion = "2.19.0"
    val log4sVersion = "1.10.0"
    val pureconfigVersion = "0.17.2"
    val javaxInjectVersion = "1"
    val scalaGuiceVersion = "5.1.0"
    lazy val monixVersion = "3.4.1"
  }

  object Exclusions {
    val slf4jExclusions: Seq[ExclusionRule] =
      Seq(ExclusionRule("org.slf4j", "slf4j-log4j"), ExclusionRule("org.slf4j", "slf4j-log4j12"))
  }

  lazy val logging: Seq[ModuleID] = Seq(
    "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0",
    "org.apache.logging.log4j" % "log4j-core" % log4jCoreVersion,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jCoreVersion,
    "org.apache.logging.log4j" % "log4j-jcl" % log4jCoreVersion,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jCoreVersion,
    "org.apache.logging.log4j" % "log4j-jul" % log4jCoreVersion,
    "org.log4s" %% "log4s" % log4sVersion
  )

  lazy val akka: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaActorVersion
  )

  private lazy val monix: Seq[ModuleID] = Seq(
    "io.monix" %% "monix" % monixVersion,
    "io.monix" %% "monix-reactive" % monixVersion
  )

  lazy val slick: Seq[ModuleID] = Seq(
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.github.tminglei" %% "slick-pg_circe-json" % slickPGVersion,
    "com.github.tminglei" %% "slick-pg" % slickPGVersion,
  )

  lazy val guice: Seq[ModuleID] = Seq(
    "javax.inject" % "javax.inject" % javaxInjectVersion,
    "net.codingwell" %% "scala-guice" % scalaGuiceVersion
  )

  lazy val commons: Seq[ModuleID] = Seq(
    "com.github.pureconfig" %% "pureconfig" % pureconfigVersion,
  ) ++ guice

  lazy val client: Seq[ModuleID] = logging ++ (commons ++ akka).map(_.excludeAll(slf4jExclusions: _*))

  lazy val server: Seq[ModuleID] = logging ++ (commons ++ akka ++ slick ++ monix).map(_.excludeAll(slf4jExclusions: _*))
}
