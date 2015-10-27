/*
 * Copyright 2015 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibm.spark.utils

import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorRef

/**
 * Represents a map between actor names and actor references.
 */
class ResponseMap extends collection.mutable.Map[String, ActorRef] {
  private val internalMap = new ConcurrentHashMap[String, ActorRef]()

  override def +=(kv: (String, ActorRef)): ResponseMap = {
    internalMap.put(kv._1, kv._2)

    this
  }

  override def -=(key: String): ResponseMap = {
    internalMap.remove(key)

    this
  }

  override def get(key: String): Option[ActorRef] =
    Option(internalMap.get(key))

  override def iterator: Iterator[(String, ActorRef)] = {
    import scala.collection.JavaConverters._
    internalMap.asScala.iterator
  }
}