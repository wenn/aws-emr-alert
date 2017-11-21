package com.wen.emr

import com.amazonaws.services.lambda.runtime.Context
import com.wen.emr.client.ClientFactory
import com.wen.emr.config.{AppConfig, SparkConfig}
import com.wen.emr.event.Trigger
import com.wen.emr.matcher.ClusterMatcher

class Alert {

  val config: AppConfig = SparkConfig

  /** Handler for AWS lambda
    *
    * @param event   [[TriggerEvent]] for EMR
    * @param context [[Context]] Lambda context
    */
  def handler(event: TriggerEvent, context: Context): Unit = send(event)

  /** Send the event.
    *
    * @param event [[TriggerEvent]] for EMR
    */
  def send(event: TriggerEvent): Unit = {
    matchEvent(event)
      .foreach {e =>
        if (hasStatus(e)) {
          sendMessage(event)
        }
      }
  }

  def sendMessage(event: TriggerEvent): Unit = ClientFactory
    .create(config)
    .sendMessage(event)

  def hasStatus(event: TriggerEvent): Boolean = Trigger.hasStatus(config, event)

  def matchEvent(event: TriggerEvent): Option[TriggerEvent] = ClusterMatcher(config)
    .matchByName(event)
}

