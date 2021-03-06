/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.matcher

import org.scalatest.{FlatSpec, MustMatchers}

import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.common.TestHelper
import com.wen.emr.config.AppConfig

class ClusterMatcherSpec
  extends FlatSpec
    with MustMatchers {

  trait Fixture {
    val configWithoutMatch = {
      val raw =
        """
          |spark.roomId=111
          |spark.token=222
          |spark.cluster.name.regex="nothing should match me"
        """.stripMargin

      new AppConfig {
        override def configFromS3: Config = ConfigFactory.parseString(raw)
      }
    }

    val configWithMatch = {
      val raw =
        """
          |spark.roomId=111
          |spark.token=222
          |spark.cluster.name.regex="Development(.*)Cluster(.*)"
        """.stripMargin

      new AppConfig {
        override def configFromS3: Config = ConfigFactory.parseString(raw)
      }
    }


  }

  it must "include event if cluster name a match" in new Fixture {
    val event = TestHelper.event("/sample-emr-event-terminated.json")
    val matcher = ClusterMatcher(configWithMatch)

    matcher.matchByName(event) must be(Some(event))
  }

  it must "exclude event if cluster name is not a match" in new Fixture {
    val event = TestHelper.event("/sample-emr-event-terminated.json")
    val matcher = ClusterMatcher(configWithoutMatch)

    matcher.matchByName(event) must be(None)
  }

  it must "include event if step name a match" in new Fixture {
    val event = TestHelper.event("/sample-emr-event-step-failed.json")
    val matcher = ClusterMatcher(configWithMatch)

    matcher.matchByName(event) must be(Some(event))
  }

  it must "exclude event if step name is not a match" in new Fixture {
    val event = TestHelper.event("/sample-emr-event-step-failed.json")
    val matcher = ClusterMatcher(configWithoutMatch)

    matcher.matchByName(event) must be(None)
  }

}
