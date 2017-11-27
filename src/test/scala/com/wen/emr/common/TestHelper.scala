/* Copyright (C) 2017 HERE Global B.V., including its affiliated companies.
 *
 * These coded instructions, statements, and computer programs contain
 * unpublished proprietary information of HERE Global B.V., and are
 * copy protected by law. They may not be disclosed to third parties
 * or copied or duplicated in any form, in whole or in part, without
 * the specific, prior written permission of HERE Global B.V.
 */
package com.wen.emr.common


import java.io.InputStream

import com.wen.emr.TriggerEvent
import com.wen.emr.event.Json

object TestHelper {

  /** [[TriggerEvent]] from resource file.
    *
    * @param resource resource path.
    * @return [[TriggerEvent]]
    */
  def event(resource: String): TriggerEvent = Json.load(inputStream(resource))


  /** [[InputStream]] from resource file
    *
    * @param resource resource path.
    * @return [[InputStream]]
    */
  def inputStream(resource: String): InputStream = getClass.getResourceAsStream(resource)
}
