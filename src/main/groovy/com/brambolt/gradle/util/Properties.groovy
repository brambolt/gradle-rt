package com.brambolt.gradle.util

class Properties {

  /**
   * Creates a new properties object by adding the parameter properties into an
   * empty collection. If the parameter properties have colliding names then
   * the values from parameters appearing later will overwrite values from
   * earlier parameter.
   *
   * @param properties The properties to combine
   * @return The combined properties
   */
  static java.util.Properties combineProperties(java.util.Properties... properties) {
    java.util.Properties combined = new java.util.Properties()
    properties.each { it -> combined << it }
    combined
  }

  /**
   * Creates a properties collection from a file.
   * @param file The file to read
   * @return The properties read from the file
   * @throws Exception If unable to open the file or load the properties
   */
  static java.util.Properties asProperties(File file) {
    asProperties(new java.util.Properties(), file)
  }

  /**
   * Loads properties from a file into an existing properties collection.
   *
   * @param properties The existing collection to load into
   * @param file The file to load from
   * @return The parameter properties after loading from the file
   * @throws Exception If unable to open or load from the file
   */
  static java.util.Properties asProperties(java.util.Properties properties, File file) {
    if (!file.exists())
      return properties
    new FileInputStream(file).withStream { is ->
      properties.load(is)
    }
    properties
  }

  /**
   * Converts a properties collection containing keys like <code>a.b.c</code>
   * into a map with keys like <code>A_B_C</code>.
   *
   * @param p The properties collection to convert
   * @return A map with converted keys and the same values
   */
  static Map asTokens(java.util.Properties p) {
    Map t = [:]
    Iterator i = p.keySet().iterator()
    while (i.hasNext()) {
      String k = i.next()
      Object v = p.get(k)
      t.put(k.replaceAll("\\.", '_').toUpperCase(), v)
    }
    t
  }

  /**
   * A barebones implementation of JSON conversion of a properties collection.
   *
   * <p>It is better to use a stronger library implementation where possible.
   * Don't use this if anything else is available that can do the trick...</p>
   *
   * <p>If the properties collection contains null values they will appear as
   * string nulls in brackets, e.g. &lt;null&gt;</p>
   *
   * @param p The properties collection to convert
   * @return A JSON-formatted string containing the key-value pairs
   */
  static String asJson(java.util.Properties p) {
    StringBuilder builder = new StringBuilder('{')
    List keys = p.keySet().asList().sort()
    int n = keys.size()
    for (int i = 0; i < n; ++i) {
      String k = keys.get(i)
      builder.append('"').append(k).append("\":\"")
      Object v = p.get(k)
      if (null == v)
        builder.append("<null>")
      else if (v instanceof String)
        builder.append(v)
      else builder.append(v.toString())
      builder.append('"')
      if (i + 1 < n)
        builder.append(',')
    }
    builder.append('}')
    builder.toString()
  }

  static Map<String, Object> checkStructure(java.util.Properties... properties) {
    checkStructure(properties.toList())
  }

  static Map<String, Set<Object>> checkStructure(List<java.util.Properties> properties) {
    Map accumulator = [
      difference: new HashSet<Object>(),
      intersection: new HashSet<Object>()
    ]
    properties.inject(accumulator) { Map<String, Set<Object>> acc, java.util.Properties p ->
      Set<Object> keys = p.keySet()
      if (acc.intersection.isEmpty())
        // The intersection is empty...
        if (acc.difference.isEmpty())
          // ... because this is the first object - set it up as the intersection:
          acc.intersection.addAll(keys)
        else
          // ... because the previous objects had nothing in common - add to the difference:
          acc.difference.addAll(keys)
      else {
        // Add everything to the difference that's not already in the intersection:
        acc.difference.addAll(keys.findAll { !acc.intersection.contains(it) })
        // Remove everything from the intersection that's not in the current:
        Set transitioning = acc.intersection.findAll { !keys.contains(it) }
        acc.intersection.removeAll(transitioning)
        acc.difference.addAll(transitioning)
      }
      acc
    }
  }
}
