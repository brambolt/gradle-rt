package com.brambolt.gradle.api.artifacts

import org.gradle.api.GradleException
import java.util.function.Function

import static com.brambolt.gradle.util.Platforms.isWindows

class Artifacts {

  static Map<String, Object> createMap(String description, Map<String, Object> inputs) {
    Map map = new MapFactory(description, inputs).create()
    Map validation = validateMap(description, map)
    if (!validation.isEmpty())
      throw new GradleException("Invalid artifact map input: $inputs $map $validation")
    map
  }

  static Map validateMap(String description, Map<String, Object> subject) {
    new MapValidation(description, subject).asMap()
  }

  static class MapFactory {

    String description

    Map<String, Object> inputs

    MapFactory(String description, Map<String, Object> inputs) {
      if (null == description || description.isEmpty())
        throw new GradleException("Empty artifact map factory description: $inputs")
      this.description = description
      this.inputs = inputs
    }

    Map<String, Object> create() {
      Map<String, Object> result = [:]
      result.computeIfAbsent('group', { k -> readGroup() })
      result.computeIfAbsent('artifactId', { k -> readArtifactId() })
      result.computeIfAbsent('version', { k -> readVersion() })
      result.computeIfAbsent('packaging', { k -> readPackaging() })
      result.computeIfAbsent('classifier', { k -> readClassifier() })
      result.computeIfAbsent('type', { k -> readType() })
      result.computeIfAbsent('dependency', { k -> createDependency(result) })
      result
    }

    String readGroup() {
      readPlatformAgnostic('group') // Null if nothing found
    }

    String readArtifactId() {
      readPlatformSpecific('artifactId')
    }

    String readVersion() {
      readPlatformAgnostic('version')
    }

    String readPackaging() {
      readPlatformSpecific('packaging')
    }

    String readClassifier() {
      readPlatformSpecific('classifier')
    }

    String readType() {
      readPlatformSpecific('type')
    }

    String readPlatformAgnostic(String key) {
      inputs.get(key) // Null if nothing found
    }

    String readPlatformSpecific(String key) {
      // It is not enough to check for String here because GStringImpl
      // should also be treated like a String (etc...):
      if (inputs.containsKey(key) && !(inputs[key] instanceof Map))
        return inputs.get(key)
      try {
        // Platform-specific alternative:
        return inputs[key][isWindows() ? 'windows' : 'unix']
      } catch (Exception x) { x.toString() } // Pass-through
      // Nothing found:
      null
    }

    /**
     * Formats dependency values into one of these short-forms:
     * <pre>
     *   group:artifactId:version
     *   group:artifactId:version@type
     *   group:artifactId:version:packaging
     *   group:artifactId:version:packaging@type
     *   group:artifactId:version:classifier
     *   group:artifactId:version:classifier@type
     *   group:artifactId:version:packaging-classifier
     *   group:artifactId:version:packaging-classifier@type
     * </pre>
     *
     * Note that we can't separate the packaging and classifier segments with
     * a colon because Gradle does not support this:
     * <preimport com.brambolt.gradle.api.artifacts.Configurations>
     *     org.gradle.api.IllegalDependencyNotation: Supplied String module notation 'com.calypso:calypso-installer:15.2.0.18:rel:unix' is invalid. Example notations: 'org.gradle:gradle-core:2.2', 'org.mockito:mockito-core:1.9.5:javadoc'.
     * </pre>
     * @param map
     * @return
     */
    static String createDependency(Map map) {
      if ([map.group, map.artifactId, map.version].contains(null))
        return null
      // Prefix with mandatory segments:
      StringBuffer b = new StringBuffer("${map.group}:${map.artifactId}:${map.version}")
      // Add packaging, classifier or both:
      boolean hasPackaging = map.packaging ? true : false
      boolean hasClassifier = map.classifier ? true : false
      if (hasPackaging & hasClassifier)
        b.append(":${map.packaging}-${map.classifier}")
      else if (hasPackaging)
        b.append(":${map.packaging}")
      else if (hasClassifier)
        b.append(":${map.classifier}")
      // Append type qualifier if present:
      b.append(map.type ? "@${map.type}": '')
      b.toString()
    }
  }

  static class MapValidation {

    String description

    Map<String, Object> subject

    MapValidation(String description, Map<String, Object> subject) {
      if (null == description || description.isEmpty())
        throw new GradleException("Missing artifact map validation description: $subject")
      this.description = description
      this.subject = subject
    }

    Map asMap() {
      Map validation = [:]
      Map messages = [
        group: 'Missing group',
        artifactId: 'Missing artifact identifier',
        version: 'Missing version'
      ]
      Function mapping = { key ->
        String value = subject.get(key)
        // Return null if the value is acceptable:
        (null == value || value.isEmpty()) ? messages[key]: null
      }
      ['group', 'artifactId', 'version'].each { key ->
        validation.computeIfAbsent(key, mapping)
      }
      validation
    }
  }
}
