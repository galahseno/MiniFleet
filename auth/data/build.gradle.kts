plugins {
    alias(libs.plugins.minifleet.android.library)
}

android {
    namespace = "id.dev.auth.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)
}