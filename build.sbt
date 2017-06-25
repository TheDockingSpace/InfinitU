import sbtcrossproject.{crossProject, CrossType}

enablePlugins(CopyPasteDetector, GitVersioning, GitBranchPrompt)

lazy val commonSettings = Seq(
  organization in ThisBuild := "space.thedocking.infinitu",
  organizationHomepage := Some(url("http://TheDocking.Space")),
  homepage := Some(url("https://github.com/TheDockingSpace/InfinitU")),
  bintrayVcsUrl := Some("git@github.com:thedockingspace/InfinitU"),
  licenses += ("LGPL-3.0", url("http://www.opensource.org/licenses/LGPL-3.0")),
  publishMavenStyle := true,
  bintrayRepository := "Universe",
  bintrayOrganization := Some("thedockingspace"),
  pomExtra := (
    <scm>
      <url>git@github.com:TheDockingSpace/InfinitU.git</url>
      <connection>git@github.com:TheDockingSpace/InfinitU.git</connection>
    </scm>
    <developers>
      <developer>
        <id>oswaldodantas</id>
        <name>Oswaldo Dantas</name>
        <url>http://vizualize.me/oswaldodantas</url>
      </developer>
    </developers>
  )
)

lazy val InfinitU = (project in file("."))
  .settings(commonSettings)
  .settings(publishArtifact := false)
  .aggregate(coreJS, coreJVM, quantumJS, quantumJVM)

lazy val core = crossProject(JSPlatform, JVMPlatform /*, NativePlatform*/ )
  .settings(commonSettings)
  .settings(scalaVersion := "2.12.1",
            //scapegoatVersion := "1.1.0",
            libraryDependencies ++= Seq(
              "org.specs2" %% "specs2-junit" % "3.8.8" % "test"))
//.nativeSettings(resolvers += Resolver.sonatypeRepo("snapshots"))

lazy val coreJS = core.js
  .settings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2")
lazy val coreJVM = core.jvm
//lazy val coreNative = core.native

lazy val quantum = crossProject(JSPlatform, JVMPlatform /*, NativePlatform*/ )
  .settings(commonSettings)
  .settings(scalaVersion := "2.12.1",
            //scapegoatVersion := "1.1.0",
            libraryDependencies ++= Seq(
              "org.specs2" %% "specs2-junit" % "3.8.8" % "test"))
//.nativeSettings(resolvers += Resolver.sonatypeRepo("snapshots"))
  .dependsOn(core)

def generateIndexTask(suffix: String) = Def.task {
  val source = baseDirectory.value / "index.html"
  val target = (crossTarget in Compile).value / "index.html"
  val log    = streams.value.log
  IO.writeLines(target, IO.readLines(source).map { line =>
    line.replace("{{quantumjs}}", s"quantumjs-$suffix.js")
  })

  log.info(s"generate with suffix: $suffix")
}

lazy val quantumJS = quantum.js
  .settings(
    scalaJSUseMainModuleInitializer := true,
    (fastOptJS in Compile) <<= (fastOptJS in Compile).dependsOn(
      generateIndexTask("fastopt")),
    (fullOptJS in Compile) <<= (fullOptJS in Compile).dependsOn(
    generateIndexTask("opt")))

lazy val quantumJVM = quantum.jvm
//lazy val quantumNative = quantum.native

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

licenses += ("LGPLv3", url("http://www.gnu.org/licenses/lgpl-3.0.html"))

resolvers ++= Seq(
  Resolver.mavenLocal,
  Resolver.sbtPluginRepo("snapshots"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.typesafeRepo("snapshots"),
  Resolver.typesafeRepo("releases"),
  Resolver.typesafeIvyRepo("releases")
)

fork in run := true

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

scalacOptions in Test ++= Seq("-Yrangepos")
