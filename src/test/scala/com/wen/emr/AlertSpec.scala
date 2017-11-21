package com.wen.emr

import scala.io.Source

import org.scalatest.{FlatSpec, MustMatchers}

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class AlertSpec
  extends FlatSpec
    with MustMatchers {

  trait Fixture {
    val context = new Context {
      override def getIdentity: CognitoIdentity = fail

      override def getLogStreamName: String = fail

      override def getClientContext: ClientContext = fail

      override def getLogger: LambdaLogger = fail()

      override def getMemoryLimitInMB: Int = fail

      override def getInvokedFunctionArn: String = fail

      override def getRemainingTimeInMillis: Int = fail

      override def getAwsRequestId: String = fail

      override def getFunctionVersion: String = fail

      override def getFunctionName: String = fail

      override def getLogGroupName: String = fail
    }


    def eventFromFile(file: String) = {
      val eventJson = {
        Source.fromInputStream(
          getClass
            .getResourceAsStream(file))
          .getLines
          .mkString
      }

      val mapper = {
        (new ObjectMapper)
          .registerModule(DefaultScalaModule)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      }

      mapper.readValue(eventJson, classOf[TriggerEvent])
    }

    object MockAlert extends Alert {
      var sent = false

      override def sendMessage(event: TriggerEvent): Unit = sent = true

      override def matchEvent(event: TriggerEvent) = Some(event)
    }

  }

  // Uncomment to test live
  ignore must "send a message" in new Fixture {
    val alert = new Alert
    val event = eventFromFile("/sample-emr-event-starting.json")

    alert.handler(event, context)
  }

  it must "send a message if status is match" in new Fixture {
    val event = eventFromFile("/sample-emr-event-starting.json")

    MockAlert.send(event)
    MockAlert.sent must be(true)
  }

  it must "not send a message if status does not match" in new Fixture {
    val event = eventFromFile("/sample-emr-event-waiting.json")

    MockAlert.send(event)
    MockAlert.sent must be(false)
  }
}
