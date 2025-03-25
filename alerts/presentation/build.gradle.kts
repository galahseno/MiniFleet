plugins {
    alias(libs.plugins.minifleet.android.feature.ui)
}

android {
    namespace = "id.dev.alerts.presentation"
}

dependencies {
    implementation(projects.alerts.domain)
    implementation(projects.core.domain)
}