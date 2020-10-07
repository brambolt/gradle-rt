package com.brambolt.gradle.util.jar

import java.nio.file.Files
import java.nio.file.FileSystem
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.jar.Attributes
import java.util.jar.Manifest

class Manifests {

  static final String UNKNOWN = 'UNKNOWN'

  static String readManifest(FileSystem zipfs, File tmpDir, Boolean deleteOnExit = true) {
    Path zipManifest = zipfs.getPath('META-INF/MANIFEST.MF')
    File tmpFile = new File(tmpDir, "${zipManifest.getFileName()}-check".toString())
    if (deleteOnExit)
      tmpFile.deleteOnExit()
    Path tmpManifest = tmpFile.toPath()
    Files.copy(zipManifest, tmpManifest, StandardCopyOption.REPLACE_EXISTING)
    tmpManifest.text
  }

  static String getVersionFromManifest(Class clss) {
      getAttribute(clss, 'Product-Version', 'Brambolt')
  }

  static String getAttribute(Class clss, String attributeName, String sectionName = null) {
    // See https://stackoverflow.com/questions/1272648/reading-my-own-jars-manifest
    String className = clss.getSimpleName() + ".class"
    String classPath = clss.getResource(className).toString()
    if (!classPath.startsWith("jar")) {
        // Class not from JAR; no known version:
        return UNKNOWN
    }
    Integer offset = classPath.lastIndexOf("!") + 1
    String manifestPath = classPath.substring(0, offset) + "/META-INF/MANIFEST.MF"
    new URL(manifestPath).openStream().withStream {
      Manifest manifest = new Manifest(it)
      Attributes attributes = (null != sectionName)
        ? manifest.getAttributes(sectionName)
        : manifest.getMainAttributes()
      if (null == attributes)
        return UNKNOWN
      // writeManifestAttributes(manifest.getMainAttributes(), 'Main')
      // writeManifestAttributes(attributes, 'Brambolt')
      return attributes.getValue(attributeName)
    }
  }

    /**
     * This is just a test facility.
     */
    static void writeManifestAttributes(Attributes attributes, String name) {
        PrintWriter w = new PrintWriter(new File('/tmp/' + name))
        Iterator it = attributes.keySet().iterator()
        while (it.hasNext()){
            Attributes.Name key = (Attributes.Name) it.next()
            String keyword = key.toString()
            Object value = attributes.get(key)
            w.println(keyword + ' : ' + value)
        }
        w.close()
    }
}
