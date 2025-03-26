package id.dev.minifleet.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import id.dev.auth.presentation.LoginScreenRoot
import id.dev.core.presentation.tracking.TrackingViewModel
import id.dev.dashboard.presentation.DashboardScreenRoot
import id.dev.maps.presentation.MapsScreenRoot
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Home else Screen.Auth,
        modifier = Modifier.fillMaxSize()
    ) {
        authGraph(navController)
        homeGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<Screen.Auth>(
        startDestination = Screen.Auth.Login
    ) {
        composable<Screen.Auth.Login> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.Dashboard) {
                        popUpTo(Screen.Auth.Login) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.homeGraph(navController: NavHostController) {
    navigation<Screen.Home>(
        startDestination = Screen.Home.Dashboard
    ) {
        composable<Screen.Home.Dashboard> {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val backStackEntry = remember { it }
            val viewModel: TrackingViewModel =
                koinViewModel(viewModelStoreOwner = navBackStackEntry ?: backStackEntry)

            DashboardScreenRoot(
                onSuccessLogout = {
                    navController.navigate(Screen.Auth.Login) {
                        popUpTo(Screen.Home.Dashboard) {
                            inclusive = true
                        }
                    }
                },
                onOpenMapsClick = { navController.navigate(Screen.Home.Maps) },
                trackingViewModel = viewModel
            )
        }
        composable<Screen.Home.Maps> {
            val backStackEntry = remember { navController.getBackStackEntry(Screen.Home.Dashboard) }
            val viewModel: TrackingViewModel =
                koinViewModel(viewModelStoreOwner = backStackEntry)

            MapsScreenRoot(
                onBackClick = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}