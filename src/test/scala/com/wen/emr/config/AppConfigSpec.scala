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
}
