package com.wen.emr

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.scalatest.{FlatSpec, MustMatchers}

import scala.io.Source

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

    val eventJson = {
      Source.fromInputStream(
        getClass
          .getResourceAsStream("/sample-emr-event-starting.json"))
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

  it must "send a message" in new Fixture {
    val alert = new Alert

    alert.handler(event, context)
  }

}
