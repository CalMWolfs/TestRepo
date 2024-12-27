plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io") {
        content {
            includeGroupByRegex("com\\.github\\..*")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.github.CalMWolfs:SkyHanniChangelogBuilder:1.1.0-test2")
}
