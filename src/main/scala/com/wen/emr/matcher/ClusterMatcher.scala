/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.matcher

import com.wen.emr.config.AppConfig
import com.wen.emr.event.RichTriggerEvent
import com.wen.emr.event.Type

case class ClusterMatcher(config: AppConfig) {

  /** Match [[RichTriggerEvent]] by `spark.cluster.name.regex`.
    *
    * @param event the [[RichTriggerEvent]] to match against.
    * @return maybe [[RichTriggerEvent]].
    */
  def matchByName(event: RichTriggerEvent): Option[RichTriggerEvent] =
    config.clusterNameRegex.r.findFirstIn(event.detail.message) match {
      case None => None
      case other => Some(event)
    }
}
