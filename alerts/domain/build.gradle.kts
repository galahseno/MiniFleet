plugins {
    alias(libs.plugins.minifleet.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}