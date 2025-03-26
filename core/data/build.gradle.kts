plugins {
    alias(libs.plugins.minifleet.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "id.dev.core.data"
}

dependencies {
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    api(projects.core.database)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.datastore.preferences)
}