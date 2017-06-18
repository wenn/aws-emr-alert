package com.wen.emr

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.s3._
import com.amazonaws.util.IOUtils
import com.wen.emr.client.ClientFactory

class Alert {
  val S3ConfigPath = "S3_CONFIG_PATH"

  /** Handler for AWS lambda
    *
    * @param event   [[TriggerEvent]] for EMR
    * @param context [[Context]] Lambda context
    */
  def handler(event: TriggerEvent, context: Context): Unit = {
    send(event)
  }

  /** Send message to client
    *
    * @param event [[TriggerEvent]] for EMR
    */
  def send(event: TriggerEvent): Unit = {
    val config = configFromS3
    val client = ClientFactory.load(config)

    client.sendMessage(event)
  }

  /** Load config from S3
    *
    * @return String of config content
    */
  def configFromS3: String =
    s3ConfigFilePath match {
      case None => throw new RuntimeException(s"$S3ConfigPath environment variable not defined.")
      case Some(configPath) => {
        val s3ConfigFile = new AmazonS3URI(configPath)
        val s3 = AmazonS3ClientBuilder.defaultClient

        val inputStream = s3
          .getObject(s3ConfigFile.getBucket, s3ConfigFile.getKey)
          .getObjectContent

        IOUtils.toString(inputStream)
      }
    }

  /** S3 config path
    *
    * @return string value of s3 config path
    */
  def s3ConfigFilePath: Option[String] = Option(System.getenv(S3ConfigPath))
}

