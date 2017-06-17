package com.wen.emr.notifier

import org.scalatest.{FlatSpec, MustMatchers}

class SparkClientSpec
  extends FlatSpec
    with MustMatchers {

  trait Fixture {
    val token = Token("11111")
    val room = Room("22222")
  }

  it must "have a json message" in new Fixture {

    val client = SparkClient(token, room)
    val actual = client.jsonMessage("goodbye world...")
    val expected = "{\"roomId\":\"22222\",\"markdown\":\"goodbye world...\"}"

    actual must be(expected)
  }

  it must "have a post message" in new Fixture {

    val json = "{\"roomId\":\"22222\",\"markdown\":\"goodbye world...\"}"
    val client = SparkClient(token, room)
    val actual = client.postMessage(json)

    actual.getHeaders("Authorization").head.getValue must be(s"Bearer $token")
    actual.getHeaders("Content-Type").head.getValue must be(s"application/json")
  }
}
