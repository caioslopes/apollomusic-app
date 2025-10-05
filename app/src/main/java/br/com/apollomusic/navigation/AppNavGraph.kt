package br.com.apollomusic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.apollomusic.feature.owner.login.OwnerLoginScreen
import br.com.apollomusic.feature.splash.SplashScreen
import br.com.apollomusic.feature.user.login.UserLoginScreen
import br.com.apollomusic.feature.welcome.WelcomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        route = Graph.Root.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToOwnerLogin = { navController.navigate(Screen.OwnerLogin.route) },
                onNavigateToUserLogin = { navController.navigate(Screen.UserLogin.route) }
            )
        }
        composable(Screen.UserLogin.route) {
            UserLoginScreen (
                onGoBack = { navController.navigate(Screen.Welcome.route) },
                navController = navController
            )
        }
        composable(Screen.OwnerLogin.route) {
            OwnerLoginScreen (
                onGoBack = { navController.navigate(Screen.Welcome.route) },
                navController = navController
            )
        }

        ownerNavGraph(navController)
        userNavGraph(navController)
    }
}