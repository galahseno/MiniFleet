import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.minifleet.android.feature.ui)
}

android {
    namespace = "id.dev.maps.presentation"

    defaultConfig {
//        val mapApiKey = gradleLocalProperties(rootDir, providers).getProperty("MAPS_API_KEY")
//
//        manifestPlaceholders += mapOf("MAPS_API_KEY" to mapApiKey)
    }
}

dependencies {
    implementation(libs.google.maps.android.compose)
    implementation(libs.coil.compose)

    implementation(projects.core.domain)
    implementation(projects.maps.domain)
}