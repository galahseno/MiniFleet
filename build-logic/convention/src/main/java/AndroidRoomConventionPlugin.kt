import androidx.room.gradle.RoomExtension
import id.dev.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("room").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
            }

           extensions.configure<RoomExtension> {
               schemaDirectory("$projectDir/schemas")
           }

            dependencies {
                "implementation"(libs.findLibrary("room.ktx").get())
                "ksp"(libs.findLibrary("room.compiler").get())
            }
        }
    }
}