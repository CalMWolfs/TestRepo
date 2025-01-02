// dummy text file for testing

plugins {
    kotlin("jvm") version "2.0.0" apply false
}

allprojects {
    group = "com.calmwolfs.testrepo"

    /**
     * The version of the project.
     * Major version
     * Minor version
     * Beta version
     */
    version = "1.0.1"

    repositories {
        mavenCentral()
        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }
}