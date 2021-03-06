/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.config

import org.scalatest.{FlatSpec, MustMatchers}

import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.notifier.{Room, Token}

class AppConfigSpec
  extends FlatSpec
    with MustMatchers {

  trait Fixture {
    val config = {
      val raw =
        """
          |spark.roomId=111
          |spark.token=222
          |spark.cluster.name.regex=abc
          |spark.cluster.status=" STARTING, WAITING    "
          |spark.cluster.step.status=" CANCELLED, FAILED    "
        """.stripMargin

      new AppConfig {
        override def configFromS3: Config = ConfigFactory.parseString(raw)
      }
    }
  }

  it must "have a room" in new Fixture {
    config.room must be(Room("111"))
  }

  it must "have a token" in new Fixture {
    config.token must be(Token("222"))
  }

  it must "have a cluster name matcher" in new Fixture {
    config.clusterNameRegex must be("abc")
  }

  it must "have a cluster statuses to watch" in new Fixture {
    config.allowClusterStatuses.sorted must be(
      Seq(
        "STARTING",
        "WAITING"
      )
    )
  }

  it must "have a step statuses to watch" in new Fixture {
    config.allowStepStatuses.sorted must be(
      Seq(
        "CANCELLED",
        "FAILED"
      ).sorted
    )
  }

  it must "have default step statuses" in {
    val config = {
      new AppConfig {
        override def configFromS3: Config = ConfigFactory.parseString("")
      }
    }

    config.allowStepStatuses must be(
      Seq("FAILED")
    )
  }
  it must "have default cluster statuses" in {
    val config = {
      new AppConfig {
        override def configFromS3: Config = ConfigFactory.parseString("")
      }
    }

    config.allowClusterStatuses.sorted must be(
      Seq(
        "STARTING",
        "TERMINATED",
        "TERMINATED_WITH_ERRORS"
      ).sorted
    )
  }

  it must "err with bad cluster status" in {
    val badConfig = {
      val raw =
        """
          |spark.cluster.status="BAD"
        """.stripMargin

      new AppConfig {
        override def configFromS3: Config = ConfigFactory.parseString(raw)
      }
    }

    val error = intercept[IllegalArgumentException] {
      badConfig.allowClusterStatuses
    }

    error.getMessage must be("Not a valid status=BAD")
  }
}
