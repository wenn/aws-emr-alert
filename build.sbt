name := "Aws Emr Alert"
organization := "com.wen.emr"
version := "0.1.0"
scalaVersion := "2.12.1"

libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "1.1.0"
assemblyJarName in assembly := "emr-alert.jar"
