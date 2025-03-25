plugins {
    alias(libs.plugins.minifleet.android.library)
    alias(libs.plugins.minifleet.android.room)
}

android {
    namespace = "id.dev.core.database"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
}