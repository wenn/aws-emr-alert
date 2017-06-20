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

  /** Send message to client
    *
    * @param event [[TriggerEvent]] for EMR
    */
  def send(event: TriggerEvent): Unit = {
    ClusterMatcher(SparkConfig).matchByName(event).foreach {e =>
      val client = ClientFactory.create(SparkConfig)
      client.sendMessage(e)
    }
  }

}

