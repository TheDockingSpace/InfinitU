import sbtcrossproject.{crossProject, CrossType}

enablePlugins(CopyPasteDetector)

lazy val core = crossProject(JSPlatform, JVMPlatform /*, NativePlatform*/ )
  .settings(scalaVersion := "2.12.1",
            //scapegoatVersion := "1.1.0",
            libraryDependencies ++= Seq(
              "org.specs2" %% "specs2-junit" % "3.8.8" % "test"))
//.nativeSettings(resolvers += Resolver.sonatypeRepo("snapshots"))

lazy val coreJS = core.js
  .settings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.1")
lazy val coreJVM = core.jvm
//lazy val coreNative = core.native

lazy val quantum = crossProject(JSPlatform, JVMPlatform /*, NativePlatform*/ )
  .settings(scalaVersion := "2.12.1",
            //scapegoatVersion := "1.1.0",
            libraryDependencies ++= Seq(
              "org.specs2" %% "specs2-junit" % "3.8.8" % "test"))
//.nativeSettings(resolvers += Resolver.sonatypeRepo("snapshots"))
  .dependsOn(core)

lazy val quantumJS  = quantum.js
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
