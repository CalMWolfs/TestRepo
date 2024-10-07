plugins {
    java
    kotlin("jvm") version "2.0.0"
    `maven-publish`
    `java-gradle-plugin`
}

group = "com.calmwolfs.testrepo"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io") {
        content {
            includeGroupByRegex("com\\.github\\..*")
        }
    }
}

dependencies {
    implementation("com.github.CalMWolfs:SkyHanniChangelogBuilder:1.0.0-test2")
}

kotlin {
    jvmToolchain(8)
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
            enableLanguageFeature("BreakContinueInInlineLambdas")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.register("checkPrDescription", ChangelogVerification::class) {
    this.prTitle = project.findProperty("prTitle") as String
    this.prBody = project.findProperty("prBody") as String
}
