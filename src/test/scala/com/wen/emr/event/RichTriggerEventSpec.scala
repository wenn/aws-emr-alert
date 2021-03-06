/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.event

import org.scalatest.{FlatSpec, MustMatchers}

import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.common.TestHelper
import com.wen.emr.config.AppConfig

class RichTriggerEventSpec
  extends FlatSpec
    with MustMatchers {

  val config = new AppConfig {
    val raw =
      """
        |spark.cluster.status="TERMINATED"
        |spark.cluster.step.status="FAILED"
      """.stripMargin

    override def configFromS3: Config = ConfigFactory.parseString(raw)
  }

  it must "have status for a step" in {
    val event = TestHelper.event("/sample-emr-event-step-failed.json")
    val status = event.hasStatus(config)

    status must be(true)
  }

  it must "not have a status for a step" in {
    val event = TestHelper.event("/sample-emr-event-step-cancelled.json")
    val status = event.hasStatus(config)

    status must be(false)
  }

  it must "have a status for a cluster" in {
    val event = TestHelper.event("/sample-emr-event-terminated.json")
    val status = event.hasStatus(config)

    status must be(true)
  }

  it must "not have a status for a cluster" in {
    val event = TestHelper.event("/sample-emr-event-starting.json")
    val status = event.hasStatus(config)

    status must be(false)
  }

  it must "have an event type for a cluster" in {
    val event = TestHelper.event("/sample-emr-event-starting.json")

    event.eventType must be(Type.Cluster)
  }

  it must "have an event type for a step" in {
    val event = TestHelper.event("/sample-emr-event-step-cancelled.json")

    event.eventType must be(Type.Step)
  }
}
