// based on sbt-cross-test/src/sbt-test/plugins.sbt

resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("org.scala-js"     % "sbt-scalajs"              % "0.6.17")
addSbtPlugin("org.scala-native" % "sbt-scalajs-crossproject" % "0.2.0")
addSbtPlugin("org.scala-native" % "sbt-crossproject"         % "0.2.0")
addSbtPlugin("org.scala-native" % "sbt-scala-native"         % "0.2.1")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.7")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
//addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")
addSbtPlugin("org.scoverage" % "sbt-scoverage"       % "1.5.0")
addSbtPlugin("com.codacy"    % "sbt-codacy-coverage" % "1.3.8")
addSbtPlugin("de.johoop"     % "cpd4sbt"             % "1.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-feature",
  "-encoding",
  "utf8"
)
