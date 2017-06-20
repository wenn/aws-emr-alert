package com.wen.emr.client

import com.typesafe.config.Config
import com.wen.emr.config.{AppConfig, SparkConfig}
import com.wen.emr.notifier.SparkClient

object ClientFactory {

  /** [[Config]] from [[SparkConfig]]
    *
    * @return [[SparkClient]]
    */
  def create(config: AppConfig): SparkClient =
    SparkClient(
      token = config.token,
      room = config.room
    )
}
