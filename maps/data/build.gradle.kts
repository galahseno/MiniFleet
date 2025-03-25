plugins {
    alias(libs.plugins.minifleet.android.library)
}

android {
    namespace = "id.dev.maps.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.maps.domain)
}