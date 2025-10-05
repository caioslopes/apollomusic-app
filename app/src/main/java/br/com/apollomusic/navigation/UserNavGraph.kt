package br.com.apollomusic.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.apollomusic.feature.user.home.UserHomeScreen

fun NavGraphBuilder.userNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.UserHome.route,
        route = Graph.User.route
    ) {
        composable(Screen.UserHome.route) {
            UserHomeScreen (
                navController = navController
            )
        }
    }
}