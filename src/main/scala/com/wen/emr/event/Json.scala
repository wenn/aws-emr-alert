package com.wen.emr.event

import java.io.InputStream

import scala.io.Source

import com.google.gson.Gson

object Json {

  private val gson = new Gson

  /** Load [[RichTriggerEvent]]
    *
    * @param is [[InputStream]] to load from.
    * @return [[RichTriggerEvent]]
    */
  def load(is: InputStream): RichTriggerEvent =
    gson.fromJson(
      Source.fromInputStream(is).mkString,
      classOf[RichTriggerEvent]
    )
}

