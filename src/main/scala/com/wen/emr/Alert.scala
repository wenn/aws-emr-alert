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
  def configFromS3: String = {
    val config = {
      val s3ConfigFilePath = System.getenv(S3ConfigPath)
      val s3ConfigFile = new AmazonS3URI(s3ConfigFilePath)
      val s3 = AmazonS3ClientBuilder.defaultClient

      val inputStream = s3
        .getObject(s3ConfigFile.getBucket, s3ConfigFile.getKey)
        .getObjectContent
      IOUtils.toString(inputStream)
    }
    config
  }
}

