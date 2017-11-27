package com.wen.emr.event

import org.scalatest.{FlatSpec, MustMatchers}

import com.wen.emr.common.TestHelper

class JsonSpec
  extends FlatSpec
    with MustMatchers {

  it must "load event from file" in {
    val is = TestHelper.inputStream("/sample-emr-event-step-failed.json")
    val event = Json.load(is)
    val detail = event.detail

    event.id must be("6965277f-1f52-4564-9f7b-ae90b0ce2294")
    event.detailType must be("EMR Step Status Change")
    event.time must be("2016-12-16T20:53:09Z")
    event.region must be("us-east-1")

    detail.severity must be("ERROR")
    detail.name must be("CustomJAR")
    detail.clusterId must be("j-1YONHTCP3YZKC")
    detail.state must be("FAILED")
    detail.message must be("Step s-36ZWOFMZ19IUZ (CustomJAR) in Amazon " +
      "EMR cluster j-1YONHTCP3YZKC (Development Cluster) " +
      "failed at 2016-12-16 20:53 UTC.")
  }
}
