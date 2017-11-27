/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.event

import com.wen.emr.TriggerEvent
import com.wen.emr.config.AppConfig

object Trigger {
  val StepDetailType = "EMR Step Status Change"
  val ClusterDetailType = "EMR Cluster State Change"

  /** `true` if status match config for cluster and step.
    *
    * @throws IllegalArgumentException if neither `StepDetailType` nor `ClusterDetailType`
    * @param event a [[Trigger]]
    * @return [[Boolean]]
    */
  def hasStatus(config: AppConfig,
                event: TriggerEvent): Boolean = {
    println(s"test 1: $event")
    event.detailType.trim match {
      case StepDetailType => config.allowStepStatuses.contains(event.detail.state)
      case ClusterDetailType => config.allowClusterStatuses.contains(event.detail.state)
      case _ => throw new IllegalArgumentException(s"[${event.detailType}] not handled.")
    }
  }
}

