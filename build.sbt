import sbtcross.{crossProject, CrossType}

lazy val core = crossProject(JSPlatform, JVMPlatform /*, NativePlatform*/ )
  .settings(scalaVersion := "2.12.1",
            libraryDependencies ++= Seq(
              "org.specs2" %% "specs2-core" % "3.8.8" % "test"))
//.nativeSettings(resolvers += Resolver.sonatypeRepo("snapshots"))

lazy val coreJS  = core.js
lazy val coreJVM = core.jvm
//lazy val coreNative = core.native

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
