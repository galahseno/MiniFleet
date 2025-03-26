plugins {
    alias(libs.plugins.minifleet.android.library)
}

android {
    namespace = "id.dev.dashboard.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.dashboard.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}