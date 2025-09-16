package br.com.apollomusic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.apollomusic.feature.owner.home.OwnerHomeScreen
import br.com.apollomusic.feature.owner.login.OwnerLoginScreen
import br.com.apollomusic.feature.user.home.UserHomeScreen
import br.com.apollomusic.feature.user.login.UserLoginScreen
import br.com.apollomusic.feature.welcome.WelcomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToOwnerLogin = { navController.navigate(Screen.OwnerLogin.route) },
                onNavigateToUserLogin = { navController.navigate(Screen.OwnerHome.route) }
            )
        }
        composable(Screen.UserLogin.route) {
            UserLoginScreen (
                onNavigateToDetail = { navController.navigate(Screen.Welcome.route) }
            )
        }
        composable(Screen.UserHome.route) {
            UserHomeScreen (
                onNavigateToDetail = { navController.navigate(Screen.Welcome.route) }
            )
        }
        composable(Screen.OwnerLogin.route) {
            OwnerLoginScreen (
                onGoBack = { navController.navigate(Screen.Welcome.route) }
            )
        }
        composable(Screen.OwnerHome.route) {
            OwnerHomeScreen(
                onNavigateToDetail = { navController.navigate(Screen.Welcome.route) }
            )
        }
    }
}