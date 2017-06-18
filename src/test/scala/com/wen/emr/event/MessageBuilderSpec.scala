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
    val terminatedJson = readFile("/sample-emr-event-terminated.json")
    val startingJson = readFile("/sample-emr-event-starting.json")

    def readFile(filePath: String) = {
      Source.fromInputStream(
        getClass
          .getResourceAsStream(filePath))
        .getLines
        .mkString
    }

    val mapper = {
      (new ObjectMapper)
        .registerModule(DefaultScalaModule)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    val terminatedEvent = mapper.readValue(terminatedJson, classOf[TriggerEvent])
    val startingEvent = mapper.readValue(startingJson, classOf[TriggerEvent])
  }

  it must "build message from terminated event" in new Fixture {
    val actual = MessageBuilder.build(terminatedEvent)
    val expected =
      """**Development Cluster**: j-1YONHTCP3YZKC
        |- TERMINATED
        |- Amazon EMR Cluster j-1YONHTCP3YZKC (Development Cluster) has terminated at 2016-12-16 21:00 UTC with a reason of USER_REQUEST.
        | """.stripMargin


    actual must be(expected)
  }

  it must "build message from starting event" in new Fixture {
    val actual = MessageBuilder.build(startingEvent)
    val expected =
      """**Development Cluster**: j-1YONHTCP3YZKC
        |- STARTING
        | """.stripMargin


    actual must be(expected)
  }

}
