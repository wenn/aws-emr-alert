/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.matcher

import com.wen.emr.TriggerEvent
import com.wen.emr.config.AppConfig

case class ClusterMatcher(config: AppConfig) {

  /** Match [[TriggerEvent]] by `spark.cluster.name.regex`
    *
    * @param event the [[TriggerEvent]] to match against
    * @return maybe [[TriggerEvent]]
    */
  def matchByName(event: TriggerEvent): Option[TriggerEvent] = {
    config.clusterNameRegex.r.findFirstIn(event.detail.name) match {
      case None => None
      case other => Some(event)
    }
  }
}
