package id.dev.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.addUiLayerDependency(project: Project) {
    "implementation"(project(":core:presentation:ui"))
    "implementation"(project(":core:presentation:design_system"))

    "implementation"(project.libs.findBundle("koin.compose").get())
    "implementation"(project.libs.findBundle("compose").get())
}