plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.github.CalMWolfs:SkyHanniChangelogBuilder:1.0.0-test2") // Add your library here too
}