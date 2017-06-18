package com.wen.emr.event

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.wen.emr.TriggerEvent
import org.scalatest.{FlatSpec, MustMatchers}

import scala.io.Source

class MessageBuilderSpec
  extends FlatSpec
    with MustMatchers {

  trait Fixture {
    val eventJson = {
      Source.fromInputStream(
        getClass
          .getResourceAsStream("/sample-emr-event.json"))
        .getLines
        .mkString
    }

    val mapper = {
      (new ObjectMapper)
        .registerModule(DefaultScalaModule)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    val event = mapper.readValue(eventJson, classOf[TriggerEvent])
  }

  it must "build message from event" in new Fixture {
    val actual = MessageBuilder.build(event)
    val expected =
      """**Development Cluster**
        |- j-1YONHTCP3YZKC
        |- TERMINATED
        |- Amazon EMR Cluster j-1YONHTCP3YZKC (Development Cluster) has terminated at 2016-12-16 21:00 UTC with a reason of USER_REQUEST.
        | """.stripMargin


    actual must be(expected)
  }

}
