package com.brambolt.gradle.util

import org.junit.jupiter.api.Test

import static com.brambolt.gradle.util.Maps.asTokens
import static com.brambolt.gradle.util.Maps.flatten
import static com.brambolt.gradle.util.Maps.raise
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

class MapsTest {

  @Test
  void testRaiseWithoutSegments() {
    Map x = [ a: 1, b: 2 ]
    Map m = [ a: 1, b: 2 ]
    Map r = raise(m)
    assertEquals(x, r)
  }

  @Test
  void testRaise1() {
    Map x = [ a: [ b: 1 ] ]
    Map m = [ 'a.b': 1 ]
    Map r = raise(m)
    assertEquals(x, r)
  }

  @Test
  void testRaise2() {
    Map x = [
      l11: [
        l21: 21,
        l22: 22,
        l23: [
          l31: 31,
          l32: 32
        ]
      ] ,
      l12: 12,
      l13: 13,
      l14: [
        l24: 24,
        l25: [
          l33: 33
        ]
      ]
    ]
    Map m = [
      'l11.l21': 21,
      'l11.l22': 22,
      'l11.l23.l31': 31,
      'l11.l23.l32': 32,
      'l12': 12,
      'l13': 13,
      'l14.l24': 24,
      'l14.l25.l33': 33
    ]
    Map r = raise(m)
    assertEquals(x, r)
  }

  @Test
  void testFlattenFlat() {
    Map m = [ a: 1, b: 2 ]
    Map f = flatten(m)
    assertEquals(m, f)
  }

  @Test
  void testFlatten1() {
    Map x = [ 'a.b': 1 ]
    Map m = [ a: [ b: 1 ] ]
    Map r = flatten(m)
    assertEquals(x, r)
  }

  @Test
  void testFlatten2() {
    Map x = [
      'l11.l21': 21,
      'l11.l22': 22,
      'l11.l23.l31': 31,
      'l11.l23.l32': 32,
      'l12': 12,
      'l13': 13,
      'l14.l24': 24,
      'l14.l25.l33': 33
    ]
    Map m = [
      l11: [
        l21: 21,
        l22: 22,
        l23: [
          l31: 31,
          l32: 32
        ]
      ] ,
      l12: 12,
      l13: 13,
      l14: [
        l24: 24,
        l25: [
          l33: 33
        ]
      ]
    ]
    Map r = flatten(m)
    assertEquals(x, r)
  }

  @Test
  void testAsTokens() {
    Map properties = [
      'a.b.c': 'abc',
      'd.e.f': 'def',
      g: 'g'
    ]
    Map tokens = asTokens(properties)
    println(properties.toString())
    println(tokens.toString())
    assertEquals(properties.size(), tokens.size())
    assertTrue(tokens.containsKey('A_B_C'))
    assertEquals(properties['a.b.c'], tokens['A_B_C'])
  }
}
