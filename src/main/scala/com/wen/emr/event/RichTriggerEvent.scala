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
import com.wen.emr.event.Type.EventType

class RichTriggerEvent extends TriggerEvent {

  private val StepDetailType = "EMR Step Status Change"
  private val ClusterDetailType = "EMR Cluster State Change"

  /** [[EventType]] of the [[RichTriggerEvent]].
    *
    * @throws IllegalArgumentException if neither `StepDetailType`
    *                                  nor `ClusterDetailType`.
    * @return [[EventType]]
    */
  def eventType: EventType = detailType.trim match {
    case StepDetailType => Type.Step
    case ClusterDetailType => Type.Cluster
    case _ => throw new IllegalArgumentException(s"[$detailType] " +
      s"not handled.")
  }

  /** `true` if status match config for cluster and step.
    *
    * @return [[Boolean]]
    */
  def hasStatus(config: AppConfig): Boolean = {
    eventType match {
      case Type.Step => config.allowStepStatuses.contains(detail.state)
      case Type.Cluster => config.allowClusterStatuses.contains(detail.state)
    }
  }
}

