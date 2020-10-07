package com.brambolt.gradle.api.artifacts

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.UnknownConfigurationException

class Configurations {

  static Configuration getOrCreate(Project project, String configurationName) {
    Configuration result
    try {
      result = project.configurations.getByName(configurationName)
    } catch (UnknownConfigurationException x) {
      // Ignore - we'll create it instead
    }
    if (null == result)
      result = project.configurations.create(configurationName)
    result
  }

  static void requireSingleFile(Configuration configuration, String description, String name) {
    requireSingleFileConfiguration(configuration, description, name)
  }

  static void requireSingleFileConfiguration(Configuration configuration, String description, String name) {
    if (null == configuration)
      throw new GradleException("$description configuration '$name' is missing from project")
    if (configuration.files.isEmpty())
      throw new GradleException("$description configuration '$name' has no dependencies")
    if (1 < configuration.files.size())
      throw new GradleException("$description configuration has multiple dependencies: ${configuration.files.toString()}")
    File f = configuration.singleFile
    if (!f.exists())
      throw new GradleException("$description configuration file not found: ${f.absolutePath}")
    if (!f.isFile())
      throw new GradleException("$description configuration file invalid: ${f.absolutePath}")
  }
}