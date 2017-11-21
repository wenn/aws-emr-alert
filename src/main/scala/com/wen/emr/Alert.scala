package com.wen.emr

import com.amazonaws.services.lambda.runtime.Context
import com.wen.emr.client.ClientFactory
import com.wen.emr.config.SparkConfig
import com.wen.emr.matcher.ClusterMatcher

class Alert {
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
    .create(SparkConfig)
    .sendMessage(event)

  def hasStatus(event: TriggerEvent): Boolean = SparkConfig
    .hasClusterStatus(event.getDetail.getState)

  def matchEvent(event: TriggerEvent): Option[TriggerEvent] = ClusterMatcher(SparkConfig)
    .matchByName(event)
}

