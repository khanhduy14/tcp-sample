ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / name := "tcp-sample"
ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(name := (ThisBuild / name).value)
  .aggregate(client)
  .aggregate(server)

lazy val client = (project in file("modules/client"))
  .settings(
    name := "client"
  ).settings(libraryDependencies ++= Dependencies.client)

lazy val server = (project in file("modules/server"))
  .settings(
    name := "server"
  ).settings(libraryDependencies ++= Dependencies.server)

lazy val assemblySettings = Seq(
  assembly / assemblyMergeStrategy := {
    case x if Assembly.isConfigFile(x) =>
      MergeStrategy.concat
    case PathList(ps @ _*)
      if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>
      MergeStrategy.rename
    case PathList("META-INF", xs @ _*) =>
      (xs map { _.toLowerCase }) match {
        case ("manifest.mf" :: Nil) | ("index.list" :: Nil) |
             ("dependencies" :: Nil) =>
          MergeStrategy.discard
        case ps @ (x :: xs)
          if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
          MergeStrategy.discard
        case "plexus" :: xs =>
          MergeStrategy.discard
        case "services" :: xs =>
          MergeStrategy.filterDistinctLines
        case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) =>
          MergeStrategy.filterDistinctLines
        case _ => MergeStrategy.discard
      }
    case _ => MergeStrategy.first
  }
)