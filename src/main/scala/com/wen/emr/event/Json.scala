package com.wen.emr.event

import java.io.InputStream

import scala.io.Source

import com.google.gson.Gson
import com.wen.emr.TriggerEvent

object Json {

  private val gson = new Gson

  /** Load [[TriggerEvent]]
    *
    * @param is [[InputStream]] to load from.
    * @return [[TriggerEvent]]
    */
  def load(is: InputStream): TriggerEvent =
    gson.fromJson(
      Source.fromInputStream(is).mkString,
      classOf[TriggerEvent]
    )
}

