package com.brambolt.gradle.text

/**
 * A collection of convenience utilities for working with strings.
 */
class Strings {

  /**
   * Porcelain for removing all whitespace from a string.
   * @param content The string content to remove all whitespace from
   * @return The parameter content with all whitespace removed
   */
  static String withoutWhitespace(String content) {
    content ? content.replaceAll('\\s+', '') : null
  }

  /**
   * Converts a delimited parameter string to CamelCase.
   *
   * <p>This is useful for converting snake_case or kebab-case strings to
   * CamelCase. Note that the first character is changed to upper case.</p>
   *
   * <p>The content is a string like <code>a-string_with|delimiters</code>.</p>
   *
   * <p>The delimiters are an array like <code>[-, _, |]</code>.</p>
   *
   * <p>In this case the output would be <code>aStringWithDelimiters</code>.
   *
   * @param needsHumps The content string to be converted
   * @param delimiters The delimiters to remove
   * @return The camel-cased content with delimiters removed
   */
  static String toCamelCase(String needsHumps, List<String> delimiters) {
    split(needsHumps, * (delimiters as List))
    // Capitalize the first letter of each segment:
      .collect { s -> s.substring(0, 1).toUpperCase() + s.substring(1) }
    // Join the segments into a camel-cased string:
      .join('')
  }

  /**
   * Splits the subject string on multiple delimiters.
   *
   * @param subject The string to split into segments
   * @param delimiters The segment delimiters
   * @return The segments of the subject string after splitting on the delimiters
   */
  static List<String> split(String subject, String... delimiters) {
    // Split on multiple delimiters, so 'a-b_c' becomes ['a', 'b', 'c']:
    delimiters.inject([subject]) { split, delimiter ->
      split.inject([]) { resplit, segment ->
        resplit.addAll(segment.split(delimiter))
        resplit
      }
    }
  }
}
