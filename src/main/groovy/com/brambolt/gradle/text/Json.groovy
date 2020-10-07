package com.brambolt.gradle.text

import groovy.json.JsonException
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.GradleException

import static com.brambolt.text.Templates.bind
import static com.brambolt.util.Resources.scan

class Json {

  static String readJsonDataFromFile(File dir, String type, String basename, Map properties) {
    String s = System.getProperty("file.separator")
    File file = new File("${dir.absolutePath}${s}${type}${s}${basename}.json")
    file.exists() ? processJson(file.text, properties) : null
  }

  static String readJsonDataFromResource(String path, String type, String basename, Map properties) {
    readJsonResource(createJsonResourcePath(path, type, basename), 'UTF-8', properties)
  }

  static String createJsonResourcePath(String path, String type, String basename) {
    StringBuilder result = new StringBuilder()
    if (null != path && !path.trim().isEmpty())
      result.append(path + '/')
    if (null != type && !type.trim().isEmpty())
      result.append(type + '/')
    if (null == basename || basename.trim().isEmpty())
      throw new GradleException("No basename provided - unable to locate resource")
    result.append(basename + '.json')
    result.toString()
  }

  static String readJsonResource(String resourcePath, String charset, Map properties) {
    processJson(scan(resourcePath, charset), properties)
  }

  static String processJson(String json, Map properties) {
    minify(bind(json, properties))
  }

  static String minify(String json) {
    try {
      JsonOutput.toJson(new JsonSlurper().parseText(json))
    } catch (JsonException x) {
      throw new GradleException("Unable to parse JSON string: [${json}]", x)
    }
  }
}
