
enablePlugins(AssemblyPlugin)

val scala3Version = "3.3.1"

ThisBuild / scalaVersion := scala3Version
assembly / assemblyJarName := "RiseOfTheMachine.jar"
assembly / assemblyMergeStrategy := {
  case "reference.conf" => MergeStrategy.concat
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass := Some("Main")
assembly / mainClass := Some("Main")
lazy val root = (project in file("."))
  .settings(
    name := "PPS-24-RiseOfTheMachine",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "com.github.sbt" % "junit-interface" % "0.13.2" % Test,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "com.github.lalyos" % "jfiglet" % "0.0.8",
    )
  )
