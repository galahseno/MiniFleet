plugins {
    alias(libs.plugins.minifleet.android.library)
}

android {
    namespace = "id.dev.core.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    api(projects.core.database)
}