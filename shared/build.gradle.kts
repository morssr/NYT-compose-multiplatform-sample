plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.8.0"
    id("app.cash.sqldelight") version "2.0.0"
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).all {
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export("dev.icerock.moko:mvvm-core:0.16.1")
            export("dev.icerock.moko:resources:0.23.0")
            export("dev.icerock.moko:graphics:0.9.0") // toUIColor here
            export(libs.calf.ui)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation("org.jetbrains.compose.ui:ui-util:1.5.1")
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.bundles.voyager)
                implementation(libs.kotlinx.dateTime)
                implementation(libs.kotlinx.serialization)

                implementation(libs.ktor.core)
                implementation(libs.ktor.logging)
                implementation(libs.ktor.contentNegotiation)
                implementation(libs.ktor.serialization)

                implementation(libs.sqlDelight.coroutines)

                implementation(libs.koin.core)
                implementation(libs.koin.test)
                implementation(libs.koin.compose)

                implementation(libs.datastore.preferences)

                implementation(libs.logger.kermit)
                implementation(libs.kamel.image)

                implementation(libs.windowSizeClass)

                implementation(libs.paging.compose.common)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.ktor.mock)
                implementation(libs.koin.test)
                implementation(libs.paging.testing)
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.sqlDelight.androidDriver)
                implementation(libs.ktor.android)
                implementation(libs.koin.android)
            }
        }

        val androidUnitTest by getting{
            dependsOn(commonTest)
            dependencies {
//                implementation(libs.androidx.core.ktx)
//                implementation("junit:junit:4.13.2")
//                implementation("org.robolectric:robolectric:4.9")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                implementation(libs.sqlDelight.nativeDriver)
                implementation(libs.ktor.ios)
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.mls.kmp.mor.nytnewskmp"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.mls.kmp.mor.nytnewskmp.database")
        }
    }
}

dependencies {
    commonMainApi(libs.moko.mvvm.core)
    commonMainApi(libs.moko.resources.compose) // for compose multiplatform
    commonTestImplementation(libs.moko.resources.test)

    commonMainApi(libs.calf.ui)
}

multiplatformResources {
    multiplatformResourcesPackage = "com.mls.kmp.mor.nytnewskmp.library" // required
//    disableStaticFrameworkWarning = true
}