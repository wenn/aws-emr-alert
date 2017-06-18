package com.wen.emr.event

import com.wen.emr.TriggerEvent

object MessageBuilder {

  val EmptyString = ""

  def build(event: TriggerEvent) = {
    val message = if (!event.detail.message.isEmpty) s"\n|- ${event.detail.message}"
                  else EmptyString

    s"""**${event.detail.name}**: ${event.detail.clusterId}
        |- ${event.detail.state}$message
        | """.stripMargin
  }

}
