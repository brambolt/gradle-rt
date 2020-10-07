package com.brambolt.gradle.api.artifacts

import org.gradle.api.GradleException
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows
import static org.junit.jupiter.api.Assertions.assertTrue

class ArtifactsMapValidationTest {

    @Test
    void testNoDescription() {
        assertThrows(GradleException.class, { new Artifacts.MapValidation(null, [:]) })
    }

    @Test
    void testEmptyDescription() {
        assertThrows(GradleException.class, { new Artifacts.MapValidation('', [:]) })
    }

    @Test
    void testValidateEmpty() {
        Map map = new Artifacts.MapValidation('testValidateEmpty', [:]).asMap()
        assertEquals(3, map.size())
    }

    @Test
    void testValidate() {
        Map inputs = [ group: 'a', artifactId: 'b', version: 'c' ]
        Map map = new Artifacts.MapValidation('testValidate', inputs).asMap()
        assertTrue(map.isEmpty())
    }
}
