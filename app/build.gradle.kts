// app/build.gradle.kts

plugins {
    // These aliases should be correctly defined in your libs.versions.toml
    // If they are not, you might need to use the full plugin ID string
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
 //   alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.ccncode.beemovie"
    compileSdk = 35 // Keeping your current compile SDK

    defaultConfig {
        applicationId = "com.ccncode.beemovie"
        minSdk = 21 // Keeping your Android 5+ min SDK
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Keep this synchronized with the Kotlin version used in your project
        // For Kotlin 1.9.x, it's typically "1.5.1". For Kotlin 2.0.x it might be higher.
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core AndroidX and Lifecycle components
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // LiveData - Required for MutableLiveData and general LiveData usage
    // Ensure this alias (androidx.lifecycle.livedata.ktx) is correctly defined in libs.versions.toml
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Compose BOM - manage Compose library versions
    // Ensure this alias (androidx.compose.bom) is correctly defined in libs.versions.toml
    implementation(platform(libs.androidx.compose.bom))

    // Activity Compose - for ComponentActivity, ActivityResultLauncher in Compose
    // Ensure this alias (androidx.activity.compose) is correctly defined in libs.versions.toml
    implementation(libs.androidx.activity.compose)

    // Core Compose UI elements
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Compose LiveData integration - for .observeAsState().
    // Version usually comes from the Compose BOM, so no explicit version needed here.
    implementation("androidx.compose.runtime:runtime-livedata")

    // Debug implementations for Compose tooling
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // BOM for androidTest as well, typically included directly
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}