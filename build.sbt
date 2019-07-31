name := "eventapp"
 
version := "1.0" 
      
lazy val `eventapp` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.2.+",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.2.+",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.2"
)

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  
