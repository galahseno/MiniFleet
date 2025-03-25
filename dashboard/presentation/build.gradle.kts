plugins {
    alias(libs.plugins.minifleet.android.feature.ui)
}

android {
    namespace = "id.dev.dashboard.presentation"
}

dependencies {
    implementation(projects.dashboard.domain)
    implementation(projects.core.domain)
}