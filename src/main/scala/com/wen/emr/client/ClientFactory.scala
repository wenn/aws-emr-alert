package com.wen.emr.client

import com.typesafe.config.{Config, ConfigFactory}
import com.wen.emr.notifier.{Room, SparkClient, Token}

object ClientFactory {

  /** [[Config]] from string
    *
    * @param raw string value of config
    * @return [[Config]]
    */
  def parse(raw: String): Config = ConfigFactory.parseString(raw)

  /** [[Config]] from string
    *
    * @param raw string value of config
    * @return [[SparkClient]]
    */
  def load(raw: String): SparkClient = sparkClient(parse(raw))

  /** [[SparkClient]] loaded from config
    *
    * @param config [[Config]]
    * @return [[SparkClient]]
    */
  private def sparkClient(config: Config) =
    SparkClient(
      token = Token(config.getString("spark.token")),
      room = Room(config.getString("spark.roomId"))
    )

}
