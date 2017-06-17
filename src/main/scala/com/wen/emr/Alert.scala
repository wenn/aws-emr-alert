package com.wen.emr

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

object Alert extends RequestHandler[TriggerEvent, Context] {

  override def handleRequest(event: TriggerEvent, context: Context): Context = {
    println(event.name)

    context
  }
}

