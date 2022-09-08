name := "Crawler"

version := "0.1"

scalaVersion := "2.13.6"
val akkaVersion           = "2.6.19"
val akkaHttpVersion       = "10.2.9"
val circeVersion          = "0.14.1"
val alpakkaVersion        = "3.0.0"
val slickVersion          = "3.3.3"
val mysqlConnectorVersion = "8.0.29"
val logbackVersion        = "1.2.11"

resolvers += ("jitpack" at "https://jitpack.io")

libraryDependencies += "mysql"                       % "mysql-connector-java" % "8.0.29"
libraryDependencies += "org.postgresql"              % "postgresql"           % "42.3.6"
libraryDependencies += "com.typesafe.akka"          %% "akka-actor"           % akkaVersion
libraryDependencies += "com.typesafe.akka"          %% "akka-http"            % akkaHttpVersion
libraryDependencies += "com.typesafe.akka"          %% "akka-stream"          % akkaVersion
libraryDependencies += "com.typesafe.akka"          %% "akka-slf4j"           % akkaVersion
libraryDependencies += "com.typesafe.akka"          %% "akka-http-spray-json" % akkaHttpVersion
libraryDependencies += "dev.zio"                    %% "zio"                  % "1.0.12"
libraryDependencies += "net.debasishg"              %% "redisclient"          % "3.42"
libraryDependencies += "com.typesafe.akka"          %% "akka-actor-typed"     % akkaVersion
libraryDependencies += "commons-codec"               % "commons-codec"        % "20041127.091804"
libraryDependencies += "com.github.t3hnar"          %% "scala-bcrypt"         % "4.3.0"
libraryDependencies += "com.typesafe.akka"          %% "akka-stream-kafka"    % alpakkaVersion
libraryDependencies += "com.github.fdietze.vectory" %% "vectory"              % "b349c76"
libraryDependencies += "org.scalatest"              %% "scalatest"            % "3.2.12" % Test
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic-extras"
).map(_ % circeVersion)
libraryDependencies ++= Seq(
  "com.typesafe.slick"    %% "slick"                % slickVersion,
  "com.typesafe.slick"    %% "slick-hikaricp"       % slickVersion,
  "mysql"                  % "mysql-connector-java" % mysqlConnectorVersion,
  "ch.qos.logback"         % "logback-classic"      % logbackVersion,
  "com.github.daddykotex" %% "courier"              % "3.2.0"
)
