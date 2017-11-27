package com.wen.emr

import java.io.OutputStream

import org.scalatest.{FlatSpec, MustMatchers}

import sun.corba.OutputStreamFactory

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}
import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.common.TestHelper
import com.wen.emr.config.AppConfig

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


    object MockAlert extends Alert {
      var sent = false

      override val config = new AppConfig {
        val raw =
          """
            |spark.cluster.status="STARTING"
            |spark.cluster.step.status="FAILED"
          """.stripMargin

        override def configFromS3: Config = ConfigFactory.parseString(raw)
      }

      override def sendMessage(event: TriggerEvent): Unit = sent = true

      override def matchEvent(event: TriggerEvent) = Some(event)
    }

  }

  it must "send a message if status is match" in new Fixture {
    val event = TestHelper.event("/sample-emr-event-starting.json")

    MockAlert.send(event)
    MockAlert.sent must be(true)
  }

  it must "not send a message if status does not match" in new Fixture {
    val event = TestHelper.event("/sample-emr-event-waiting.json")

    MockAlert.send(event)
    MockAlert.sent must be(false)
  }
}
