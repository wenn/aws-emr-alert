package com.wen.emr.client

import org.scalatest.{FlatSpec, MustMatchers}

import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.config.AppConfig
import com.wen.emr.notifier.{Room, Token}

class ClientFactorySpec
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

  it must "load spark client" in new Fixture {

    val client = ClientFactory.create(config)

    client.room must be(Room("111"))
    client.token must be(Token("222"))
  }
}
