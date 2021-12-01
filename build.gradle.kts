import io.gitlab.arturbosch.detekt.Detekt

buildscript {
    repositories {
        google()
        // TODO only for pov lib
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugin.android.gradle)
        classpath(libs.plugin.kotlin.gradle)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.18.1"
}

allprojects {
    repositories {
        google()
        // TODO only for pov lib
        jcenter()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencies {
    detektPlugins(libs.plugin.detekt.formatting.gradle)
}

val configFile = file("$rootDir/config/detekt/detekt.yml")

val detektAll by tasks.registering(Detekt::class) {
    description = "Runs over whole code base without the starting overhead for each module."
    parallel = true
    autoCorrect = true
    buildUponDefaultConfig = true
    setSource(files(projectDir))
    config.setFrom(listOf(configFile))
    include("**/*.kt")
    include("**/*.kts")
    exclude("resources/")
    exclude("build/")
    reports {
        xml.enabled = false
        html.enabled = false
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
