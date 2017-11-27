package com.wen.emr.notifier

import org.scalatest.{FlatSpec, MustMatchers}

import com.wen.emr.common.TestHelper

class SparkClientSpec
  extends FlatSpec
    with MustMatchers {

  trait Fixture {
    val token = Token("11111")
    val room = Room("22222")
    val client = SparkClient(token, room)

    val terminatedEvent = TestHelper.event("/sample-emr-event-terminated.json")
    val startingEvent = TestHelper.event("/sample-emr-event-starting.json")
  }

  it must "have a json message" in new Fixture {

    val actual = client.jsonMessage("goodbye world...")
    val expected = "{\"roomId\":\"22222\",\"markdown\":\"goodbye world...\"}"

    actual must be(expected)
  }

  it must "have a post message" in new Fixture {

    val json = "{\"roomId\":\"22222\",\"markdown\":\"goodbye world...\"}"
    val actual = client.postMessage(json)

    actual.getHeaders("Authorization").head.getValue must be(s"Bearer $token")
    actual.getHeaders("Content-Type").head.getValue must be(s"application/json")
  }

  it must "build message from terminated event" in new Fixture {
    val actual = client.build(terminatedEvent)
    val expected =
      """**Development Cluster**: [j-1YONHTCP3YZKC](https://console.aws.amazon.com/elasticmapreduce/home?region=us-east-1#cluster-details:j-1YONHTCP3YZKC) - TERMINATED *at 2016-12-16T21:00:23Z*
        |- Amazon EMR Cluster j-1YONHTCP3YZKC (Development Cluster) has terminated at 2016-12-16 21:00 UTC with a reason of USER_REQUEST.""".stripMargin


    actual must be(expected)
  }

  it must "build message from starting event" in new Fixture {
    val actual = client.build(startingEvent)
    val expected = s"**Development Cluster**: [j-1YONHTCP3YZKC](https://console.aws.amazon.com/elasticmapreduce/home?region=us-east-1#cluster-details:j-1YONHTCP3YZKC) - STARTING *at 2016-12-16T21:00:23Z*"

    actual must be(expected)
  }
}
