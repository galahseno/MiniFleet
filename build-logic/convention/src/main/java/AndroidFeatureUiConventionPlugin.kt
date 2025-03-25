import id.dev.convention.addUiLayerDependency
import id.dev.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(libs.findPlugin("minifleet.android.library.compose").get().get().pluginId)
            }

           dependencies {
               addUiLayerDependency(target)
           }
        }
    }
}