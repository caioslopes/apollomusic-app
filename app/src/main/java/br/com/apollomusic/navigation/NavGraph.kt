package br.com.apollomusic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import br.com.apollomusic.feature.login.LoginScreen
import br.com.apollomusic.feature.owner.home.OwnerHomeScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToDetail = { navController.navigate(Screen.OwnerHome.route) }
            )
        }
        composable(Screen.OwnerHome.route) {
            OwnerHomeScreen(
                onNavigateToDetail = { navController.navigate(Screen.Login.route) }
            )
        }
    }
}