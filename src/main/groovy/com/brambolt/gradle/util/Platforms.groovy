package com.brambolt.gradle.util

class Platforms {

    static Boolean isWindows() {
        System.getProperty('os.name').toLowerCase().contains('windows')
    }
}