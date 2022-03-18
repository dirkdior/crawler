name := "Crawler"

version := "0.1"

scalaVersion := "2.13.6"
val akkaVersion     = "2.6.18"
val akkaHttpVersion = "10.2.6"
val circeVersion    = "0.14.1"
val alpakkaVersion  = "3.0.0"

libraryDependencies += "mysql"              % "mysql-connector-java" % "5.1.24"
libraryDependencies += "org.postgresql"     % "postgresql"           % "42.2.23"
libraryDependencies += "com.typesafe.akka" %% "akka-actor"           % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream"          % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j"           % akkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
libraryDependencies += "dev.zio"           %% "zio"                  % "1.0.12"
libraryDependencies += "net.debasishg"     %% "redisclient"          % "3.41"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed"     % akkaVersion
libraryDependencies += "commons-codec"      % "commons-codec"        % "1.15"
libraryDependencies += "com.github.t3hnar" %% "scala-bcrypt"         % "4.1"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka"    % alpakkaVersion
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic-extras"
).map(_ % circeVersion)
