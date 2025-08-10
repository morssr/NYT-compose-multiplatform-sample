import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinSerialization)
    id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
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
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
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
            implementation(libs.koin.compose)

            implementation(libs.datastore.preferences)

            implementation(libs.logger.kermit)
            implementation(libs.kamel.image)

            implementation(libs.windowSizeClass)

            implementation(libs.paging.compose.common)

            implementation(libs.calf.ui)
            implementation(libs.calf.webview)
        }

        commonTest.dependencies {
            implementation(libs.ktor.mock)
            implementation(libs.koin.test)
            implementation(libs.paging.testing)

        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.sqlDelight.androidDriver)
            implementation(libs.ktor.android)
            implementation(libs.koin.android)
            implementation(libs.androidx.splashScreen)

        }

//        androidUnitTest.dependencies {
////                implementation(libs.androidx.core.ktx)
////                implementation("junit:junit:4.13.2")
////                implementation("org.robolectric:robolectric:4.9")
//            implementation(libs.sqlDelight.androidDriver)
//        }

        iosMain.dependencies {
            implementation(libs.sqlDelight.nativeDriver)
            implementation(libs.ktor.ios)

        }

        iosTest.dependencies {
            implementation(libs.sqlDelight.nativeDriver)
        }
    }
}

android {
    namespace = "com.mls.kmp.mor.nytnewskmp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mls.mor.nytnews"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 9
        versionName = "2.0.1"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    commonMainApi(libs.moko.resources.compose)
    commonTestImplementation(libs.moko.resources.test)
}

multiplatformResources {
    resourcesPackage.set("com.mls.kmp.mor.nytnewskmp.library")
}