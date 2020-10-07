package com.brambolt.gradle.util

class Maps {

  static List copyKeys(Map map) {
    (List) map.keySet().inject([]) { kk, k ->
      kk.add(k)
      kk
    }
  }

  static Map raise(Map map) {
    // Copy the key set to avoid concurrent modification exception:
    List keys = copyKeys(map)
    keys.each { key ->
      if ((key instanceof String) && key.contains('.')) {
        // Remove the value of the segmented key:
        Object value = map.remove(key)
        // Split the key into segments:
        List segments = key.split("\\.").toList()
        // Create one-dimensional raise from the segments:
        List reversed = segments.reverse()
        Map raised = (Map) reversed.inject(value) { v, s ->
          Map m = [:]
          m.put(s, v)
          m
        }
        // Merge the raise into the map:
        merge(segments, raised, 0, map)
      }
    }
    map
  }

  // Recursive merge for raising:
  private static Map merge(
    List<String> segments,
    // The rest of the raised map:
    Map r,
    // The current key segment index:
    Integer i,
    // The target map to merge into:
    Map t) {
    // Get the current key segment for the recursion level:
    String segment = segments[i]
    // Advance the segment index for the next step:
    Integer j = i + 1
    // Are we at the bottom of the recursion?
    if (segments.size() == j) {
      // Yes - terminate, we're done:
      t << r // Can replace current value
    } else if (!t.containsKey(segment))
      // No further regression needed:
      t << r // Done
    else {
      // The raised and target maps both contain the key
      // segment, we either merge or replace:
      Object v = t[segment]
      if (v instanceof Map)
        // Both values are maps - merge:
        merge(segments, (Map) r[segment], j, (Map) v)
      else
        // The existing value is not a map - replace:
        t << r
    }
    t
  }

  static Map flatten(Map map) {
    // Copy the key set to avoid concurrent modification exception:
    List keys = copyKeys(map)
    keys.each { key ->
      Object value = map.get(key)
      if (key instanceof String && value instanceof Map) {
        map.remove(key)
        Map replacements = generate((String) key, (Map) value, [:])
        map << replacements
      }
    }
    map
  }

  // Recursive generation for flattening:
  private static Map generate(String prefix, Map level, Map r) {
    level.each { k, v ->
      String suffixed = "$prefix.$k"
      if (v instanceof Map) {
        generate(suffixed, (Map) v, r)
      } else r.put(suffixed, v) // Bottomed out
    }
    r
  }

  static Map asTokens(Map<String, Object> m) {
    Map t = [:]
    Iterator i = m.keySet().iterator()
    while (i.hasNext()) {
      String k = i.next()
      Object v = m.get(k)
      t.put(k.replaceAll("\\.", '_').toUpperCase(), v)
    }
    t
  }
}
