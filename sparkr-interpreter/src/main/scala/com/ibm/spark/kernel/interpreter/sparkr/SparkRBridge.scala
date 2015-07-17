/*
 * Copyright 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ibm.spark.kernel.interpreter.sparkr

import com.ibm.spark.interpreter.broker.BrokerBridge
import com.ibm.spark.kernel.api.KernelLike
import org.apache.spark.SparkContext

/**
 * Represents constants for the SparkR bridge.
 */
object SparkRBridge {
  /** Represents the maximum amount of code that can be queued for Python. */
  val MaxQueuedCode = 500

  /** Contains the bridge used by the current R process. */
  @volatile private var _sparkRBridge: Option[SparkRBridge] = None

  /** Allows kernel to set bridge dynamically. */
  private[sparkr] def sparkRBridge_=(newSparkRBridge: SparkRBridge): Unit = {
    _sparkRBridge = Some(newSparkRBridge)
  }

  /** Clears the bridge currently hosted statically. */
  private[sparkr] def reset(): Unit = _sparkRBridge = None

  /** Must be exposed in a static location for RBackend to access. */
  def sparkRBridge: SparkRBridge = {
    assert(_sparkRBridge.nonEmpty, "SparkRBridge has not been initialized!")
    _sparkRBridge.get
  }
}

/**
 * Represents the API available to SparkR to act as the bridge for data
 * between the JVM and R.
 *
 * @param _kernel The kernel API to expose through the bridge
 * @param _sparkContext The SparkContext to expose through the bridge
 */
class SparkRBridge(
  private val _kernel: KernelLike,
  private val _sparkContext: SparkContext
) extends BrokerBridge(_kernel, _sparkContext) {
  override val brokerName: String = "SparkR"
}