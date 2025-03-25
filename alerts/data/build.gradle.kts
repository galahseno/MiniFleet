plugins {
    alias(libs.plugins.minifleet.android.library)
}

android {
    namespace = "id.dev.alerts.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.alerts.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}