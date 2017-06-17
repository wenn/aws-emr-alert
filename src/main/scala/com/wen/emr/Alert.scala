package com.wen.emr

import com.amazonaws.services.lambda.runtime.Context

class Alert {
  def handler(event: TriggerEvent, context: Context): Unit = {
    println(event.detail)
  }
}

