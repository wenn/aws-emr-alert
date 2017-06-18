package com.wen.emr.client

import com.typesafe.config.Config
import com.wen.emr.notifier.{Room, Token}
import org.scalatest.{FlatSpec, MustMatchers}

class ClientFactorySpec
  extends FlatSpec
    with MustMatchers {

  trait Fixture {
    val raw =
      """
        |spark.roomId=111
        |spark.token=222
      """.stripMargin
  }

  it must "load spark client" in new Fixture {
    val client = ClientFactory.load(raw)

    client.room must be(Room("111"))
    client.token must be(Token("222"))
  }

  it must "parse config from string" in new Fixture {
    val config: Config = ClientFactory.parse(raw)

    config.getString("spark.roomId") must be("111")
    config.getString("spark.token") must be("222")
  }

}
