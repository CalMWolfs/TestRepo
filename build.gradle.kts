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
    this.outputDirectory.set(layout.buildDirectory)
    this.prTitle = project.findProperty("prTitle") as String
    this.prBody = project.findProperty("prBody") as String
}

tasks.register("generateChangelog", ChangelogGeneration::class) {
    this.outputDirectory.set(layout.buildDirectory)
    project.findProperty("modVersion")?.let { this.modVersion = it as String }
    project.findProperty("outputType")?.let { this.outputType = it as String }
}
