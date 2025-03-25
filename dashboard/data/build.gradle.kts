plugins {
    alias(libs.plugins.minifleet.android.library)
}

android {
    namespace = "id.dev.dashboard.data"
}

dependencies {
    implementation(projects.dashboard.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}