/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.common


import scala.io.Source

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.wen.emr.TriggerEvent

object TestHelper {

  /** [[TriggerEvent]] from resource file.
    *
    * @param resource resource path.
    * @return [[TriggerEvent]]
    */
  def event(resource: String): TriggerEvent = {
    val eventJson = {
      Source.fromInputStream(
        getClass
          .getResourceAsStream(resource))
        .getLines
        .mkString
    }

    val mapper = {
      (new ObjectMapper)
        .registerModule(DefaultScalaModule)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    mapper.readValue(eventJson, classOf[TriggerEvent])
  }
}
