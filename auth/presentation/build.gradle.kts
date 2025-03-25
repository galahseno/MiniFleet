plugins {
    alias(libs.plugins.minifleet.android.feature.ui)
}

android {
    namespace = "id.dev.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)

    testImplementation(projects.core.domain)
}