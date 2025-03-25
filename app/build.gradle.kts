plugins {
    alias(libs.plugins.minifleet.android.application.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "id.dev.minifleet"
    defaultConfig {
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlinx.serialization.json)

    // Coil
    implementation(libs.coil.compose)

    // Compose
    implementation(libs.bundles.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Koin
    implementation(libs.bundles.koin.compose)

    // Location
    implementation(libs.google.android.gms.play.services.location)

    // Timber
    implementation(libs.timber)

    implementation(projects.auth.data)
    implementation(projects.auth.domain)
    implementation(projects.auth.presentation)

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)

    implementation(projects.maps.data)
    implementation(projects.maps.domain)
    implementation(projects.maps.presentation)

    implementation(projects.dashboard.data)
    implementation(projects.dashboard.domain)
    implementation(projects.dashboard.presentation)

    implementation(projects.alerts.data)
    implementation(projects.alerts.domain)
    implementation(projects.alerts.presentation)
}