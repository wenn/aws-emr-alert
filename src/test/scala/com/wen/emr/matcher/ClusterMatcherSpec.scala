/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.matcher

import scala.io.Source

import org.scalatest.{FlatSpec, MustMatchers}

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.TriggerEvent
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
          |spark.cluster.name.regex="^Dev(.*)Cluster(.*)"
        """.stripMargin

      new AppConfig {
        override def configFromS3: Config = ConfigFactory.parseString(raw)
      }
    }


    val event = {
      def readFile(filePath: String) = {
        Source.fromInputStream(
          getClass
            .getResourceAsStream(filePath))
          .getLines
          .mkString
      }

      val eventJson = readFile("/sample-emr-event-terminated.json")

      val mapper = {
        (new ObjectMapper)
          .registerModule(DefaultScalaModule)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      }

      mapper.readValue(eventJson, classOf[TriggerEvent])
    }
  }


  it must "include event if cluster name a match" in new Fixture {
    val matcher = ClusterMatcher(configWithMatch)
    matcher.matchByName(event) must be(Some(event))
  }

  it must "exclude event if cluster name is not a match" in new Fixture {
    val matcher = ClusterMatcher(configWithoutMatch)
    matcher.matchByName(event) must be(None)
  }

}
