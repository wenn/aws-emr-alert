name := "Aws Emr Alert"
organization := "com.wen.emr"
version := "0.1.0"
scalaVersion := "2.11.8"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.3"
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.1"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += "com.amazonaws" % "aws-java-sdk-s3" % "1.11.149"

libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.8" % Test
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.3" % Test

test in assembly := {}
assemblyJarName in assembly := "emr-alert.jar"



