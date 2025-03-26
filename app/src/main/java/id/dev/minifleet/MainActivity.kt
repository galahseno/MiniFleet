package id.dev.minifleet

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import id.dev.core.presentation.design_system.MiniFleetTheme
import id.dev.minifleet.navigation.NavigationRoot
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.state.isCheckingAuth
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 500L
                    doOnEnd { screen.remove() }
                }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                ).apply {
                    interpolator = OvershootInterpolator()
                    duration = 500L
                    doOnEnd {
                        screen.remove()
                    }
                }

                zoomX.start()
                zoomY.start()
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniFleetTheme {
                KoinContext {
                    val navController = rememberNavController()
                    viewModel.state.isLoggedIn?.let {
                        NavigationRoot(
                            navController = navController,
                            isLoggedIn = it
                        )
                    }
                }
            }
        }
    }
}
