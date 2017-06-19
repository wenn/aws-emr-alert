package com.wen.emr.config

import com.amazonaws.services.s3.{AmazonS3ClientBuilder, AmazonS3URI}
import com.amazonaws.util.IOUtils
import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.notifier.{Room, Token}

/** Loads config for Cisco Spark form s3 */
trait AppConfig {
  val S3ConfigPath = "S3_CONFIG_PATH"

  lazy val config = configFromS3
  lazy val token = Token(config.getString("spark.token"))
  lazy val room = Room(config.getString("spark.roomId"))
  lazy val clusterNameRegex = config.getString("spark.cluster.name.regex")

  /** Load config from S3
    *
    * @return String of config content
    */
  def configFromS3: Config =
  s3ConfigFilePath match {
    case None => throw new RuntimeException(s"$S3ConfigPath environment variable not defined.")
    case Some(configPath) => {
      val s3ConfigFile = new AmazonS3URI(configPath)
      val s3 = AmazonS3ClientBuilder.defaultClient

      val inputStream = s3
        .getObject(s3ConfigFile.getBucket, s3ConfigFile.getKey)
        .getObjectContent

      ConfigFactory.parseString(IOUtils.toString(inputStream))
    }
  }

  /** S3 config path
    *
    * @return string value of s3 config path
    */
  def s3ConfigFilePath: Option[String] = Option(System.getenv(S3ConfigPath))
}

object SparkConfig extends AppConfig
