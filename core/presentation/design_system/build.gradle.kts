plugins {
    alias(libs.plugins.minifleet.android.library.compose)
}

android {
    namespace = "id.dev.core.presentation.design_system"
}

dependencies {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
}