package com.wen.emr


import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.Context
import com.wen.emr.client.ClientFactory
import com.wen.emr.config.{AppConfig, SparkConfig}
import com.wen.emr.event.{Json, RichTriggerEvent}
import com.wen.emr.matcher.ClusterMatcher

class Alert {

  val config: AppConfig = SparkConfig

  /** Handler for AWS lambda
    *
    * @param input   [[InputStream]] of an event object from EMR
    * @param output  [[OutputStream]] the return output stream ( not used here ).
    * @param context [[Context]] Lambda context
    */
  def handler(input: InputStream, output: OutputStream, context: Context): Unit =
    send(Json.load(input))

  /** Send the event.
    *
    * @param event [[RichTriggerEvent]] for EMR
    */
  def send(event: RichTriggerEvent): Unit = {
    matchEvent(event)
      .foreach {e =>
        if (hasStatus(e)) {
          sendMessage(event)
        }
      }
  }

  def sendMessage(event: RichTriggerEvent): Unit =
    ClientFactory
      .create(config)
      .sendMessage(event)

  def hasStatus(event: RichTriggerEvent): Boolean =
    event.hasStatus(config)

  def matchEvent(event: RichTriggerEvent): Option[RichTriggerEvent] =
    ClusterMatcher(config)
      .matchByName(event)
}

