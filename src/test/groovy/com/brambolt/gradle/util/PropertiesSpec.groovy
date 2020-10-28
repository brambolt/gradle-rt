package com.brambolt.gradle.util

import spock.lang.Specification

import static Properties.checkStructure

class PropertiesSpec extends Specification {

  def 'can check structured'() {
    given:
    java.util.Properties p1 = new java.util.Properties()
    p1.setProperty('n1', 'n1-v1')
    p1.setProperty('n2', 'n2-v1')
    java.util.Properties p2 = new java.util.Properties()
    p2.setProperty('n1', 'n1-v1') // Same value
    p2.setProperty('n2', 'n2-v2') // Different value
    when:
    Map<String, Object> results = checkStructure(p1, p2)
    then:
    results.containsKey('intersection')
    results.containsKey('difference')
    results.intersection.size() == p1.size()
    results.difference.size() == 0
  }

  def 'can detect semistructured'() {
    given:
    java.util.Properties p1 = new java.util.Properties()
    p1.setProperty('n1', 'n1-v1')
    p1.setProperty('n2', 'n2-v1')
    p1.setProperty('n3', 'n3-v1')
    p1.setProperty('n4', 'n4-v1')
    java.util.Properties p2 = new java.util.Properties()
    p2.setProperty('n1', 'n1-v1') // Same value
    p2.setProperty('n2', 'n2-v2') // Different value
    p2.setProperty('n5', 'n5-v1')
    when:
    Map<String, Object> results = checkStructure(p1, p2)
    then:
    results.containsKey('intersection')
    results.containsKey('difference')
    results.intersection.size() == 2
    results.difference.size() == 3
  }

  def 'can check disjoint'() {
    given:
    java.util.Properties p1 = new java.util.Properties()
    p1.setProperty('n3', 'n3-v1')
    p1.setProperty('n4', 'n4-v1')
    java.util.Properties p2 = new java.util.Properties()
    p2.setProperty('n5', 'n5-v1')
    when:
    Map<String, Object> results = checkStructure(p1, p2)
    then:
    results.containsKey('intersection')
    results.containsKey('difference')
    results.intersection.size() == 0
    results.difference.size() == 3
  }
}
