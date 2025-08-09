buildscript {
    dependencies {
        classpath (libs.moko.resources.generator)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}