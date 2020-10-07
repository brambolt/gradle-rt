package com.brambolt.gradle.api.artifacts

import org.gradle.api.GradleException
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows

class ArtifactsTest {

    @Test
    void testCreateMap() {
        Map inputs = [ group: 'a', artifactId: 'b', version: 'c' ]
        Map map = Artifacts.createMap('testCreateMap', inputs)
        assertEquals('a', map.group)
        assertEquals('b', map.artifactId)
        assertEquals('c', map.version)
        assertEquals('a:b:c', map.dependency)
    }

    @Test
    void testCreateMapWithoutGroup() {
        Map inputs = [ artifactId: 'b', version: 'c' ]
        assertThrows(GradleException.class, { Artifacts.createMap('testCreateMap', inputs) })
    }

    @Test
    void testCreateMapWithoutArtifactId() {
        Map inputs = [ group: 'a', version: 'c' ]
        assertThrows(GradleException.class, { Artifacts.createMap('testCreateMap', inputs) })
    }

    @Test
    void testCreateMapWithoutVersion() {
        Map inputs = [ group: 'a', artifactId: 'b' ]
        assertThrows(GradleException.class, { Artifacts.createMap('testCreateMap', inputs) })
    }
}
