plugins {
    `kotlin-dsl`
}

group = "id.dev.minifleet.build-logic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "minifleet.android.application"
            implementationClass = "AndroidAppConventionPlugin"
        }
        register("androidAppCompose") {
            id = "minifleet.android.application.compose"
            implementationClass = "AndroidAppComposeConventionPlugin"
        }
        register("androidLib") {
            id = "minifleet.android.library"
            implementationClass = "AndroidLibConventionPlugin"
        }
        register("androidLibCompose") {
            id = "minifleet.android.library.compose"
            implementationClass = "AndroidLibComposeConventionPlugin"
        }
        register("androidFeatureUi") {
            id = "minifleet.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom") {
            id = "minifleet.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("jvmLibrary") {
            id = "minifleet.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
