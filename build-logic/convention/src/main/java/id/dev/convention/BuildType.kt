package id.dev.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }
        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                commonExtension,
                                ExtensionType.APPLICATION
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                ExtensionType.APPLICATION
                            )
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                commonExtension,
                                ExtensionType.LIBRARY
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                ExtensionType.LIBRARY
                            )
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(
                                commonExtension,
                                ExtensionType.DYNAMIC_FEATURE
                            )
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension,
                                ExtensionType.DYNAMIC_FEATURE
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    if (extensionType != ExtensionType.DYNAMIC_FEATURE) {
        isMinifyEnabled = false
        proguardFiles(
            commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType
) {
    if (extensionType != ExtensionType.DYNAMIC_FEATURE) {
        isMinifyEnabled = true
        proguardFiles(
            commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
}