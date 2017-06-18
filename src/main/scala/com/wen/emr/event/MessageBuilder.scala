package com.wen.emr.event

import com.wen.emr.TriggerEvent

object MessageBuilder {

  def build(event: TriggerEvent) = {
    s"""**${event.detail.name}**
        |- ${event.detail.clusterId}
        |- ${event.detail.state}
        |- ${event.detail.message}
        | """.stripMargin
  }

}
