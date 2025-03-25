package id.dev.minifleet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import id.dev.minifleet.navigation.NavigationRoot
import id.dev.minifleet.ui.theme.MiniFleetTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniFleetTheme {
                KoinContext {
                    val navController = rememberNavController()
                    NavigationRoot(
                        navController = navController,
                        isLoggedIn = true
                    )
                }
            }
        }
    }
}
