/*
 * Copyright 2017-2020 Brambolt ehf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brambolt.gradle

import org.gradle.api.GradleException

/**
 * Utility class with functions to convert objects to intended property types.
 */
class SpecObjects {

  /**
   * Converts a spec object to a file, if possible. The spec can be a string
   * path, a file object or a closure that yields a path or a file.
   * @param propertyName The property name, only used for an exception message
   * @param spec The object to convert to a file
   * @return The file produced by converting the spec object; never null
   * @throws org.gradle.api.GradleException If the spec object can't be converted to a file
   */
  static File asFile(String propertyName, Object spec) {
    File file = asFile(spec)
    if (null == file)
      throw new GradleException(
        "Configure ${propertyName} property with ${propertyName} = <path, file or closure>")
    file
  }


  /**
   * Converts a spec object to a file, if possible. The spec can be a string
   * path, a file object or a closure that yields a path or a file. The function
   * returns null if it for some reason can't produce a file.
   * @param spec The object to convert to a file
   * @return The file produced by converting the spec object, or null
   */
  static File asFile(Object spec) {
    switch (spec) {
      case { it instanceof String || it instanceof GString }:
        return new File(spec as String)
      case { it instanceof File }:
        return spec as File
      case { it instanceof Closure }:
        return asFile((spec as Closure).call())
      default:
        return null
    }
  }
}