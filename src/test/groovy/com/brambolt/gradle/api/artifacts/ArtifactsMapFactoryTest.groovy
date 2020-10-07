package com.brambolt.gradle.api.artifacts

import org.gradle.api.GradleException
import org.junit.jupiter.api.Test

import static com.brambolt.gradle.util.Platforms.isWindows
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

class ArtifactsMapFactoryTest {

    @Test
    void testNoDescription() {
        assertThrows(GradleException.class, { new Artifacts.MapFactory(null, [:]) })
    }

    @Test
    void testEmptyDescription() {
        assertThrows(GradleException.class, { new Artifacts.MapFactory('', [:]) })
    }

    @Test
    void testCreateEmpty() {
        Map map = new Artifacts.MapFactory('testCreate1', [:]).create()
        assertTrue(map.isEmpty())
    }

    @Test
    void testPlatformAgnostic() {
        Map inputs = [ a: 'x' ]
        Artifacts.MapFactory f = new Artifacts.MapFactory('testPlatformAgnostic', inputs)
        String value = f.readPlatformAgnostic('a')
        assertEquals('x', value)
    }

    @Test
    void testPlatformSpecific() {
        Map inputs = [ a: [ unix: 'x', windows: 'y' ] ]
        Artifacts.MapFactory f = new Artifacts.MapFactory('testPlatformSpecific', inputs)
        String value = f.readPlatformSpecific('a')
        assertEquals(isWindows() ? 'y' : 'x', value)
    }

    @Test
    void testPlatformSpecificOverride() {
        Map inputs = [ a: 'x' ]
        Artifacts.MapFactory f = new Artifacts.MapFactory('testPlatformSpecificOverride', inputs)
        String value = f.readPlatformSpecific('a')
        assertEquals( 'x', value)
    }

    @Test
    void testDependency1() {
        Map inputs = [ group: 'a', artifactId: 'b', version: 'c' ]
        Map map = new Artifacts.MapFactory('testCreate1', inputs).create()
        assertEquals(4, map.size())
        assertEquals('a', map.group)
        assertEquals('b', map.artifactId)
        assertEquals('c', map.version)
        assertEquals('a:b:c', map.dependency)
    }

    @Test
    void testDependency2() {
        Map inputs = [
            group: 'a', artifactId: 'b', version: 'c',
            packaging: 'd', classifier: 'e', type: 'f'
        ]
        Map map = new Artifacts.MapFactory('testCreate1', inputs).create()
        assertEquals(7, map.size())
        assertEquals('a', map.group)
        assertEquals('b', map.artifactId)
        assertEquals('c', map.version)
        assertEquals('d', map.packaging)
        assertEquals('e', map.classifier)
        assertEquals('f', map.type)
        assertEquals('a:b:c:d-e@f', map.dependency)
    }
}
