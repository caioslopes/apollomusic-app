package br.com.apollomusic.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import br.com.apollomusic.feature.owner.home.OwnerHomeScreen

fun NavGraphBuilder.ownerNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.OwnerHome.route,
        route = Graph.Owner.route
    ) {
        composable(
            route = Screen.OwnerHome.route,
            arguments = listOf(navArgument(Screen.OwnerHome.ARG_SPOTIFY_CODE) {
                type = NavType.StringType
                defaultValue = null
                nullable = true
            })
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString(Screen.OwnerHome.ARG_SPOTIFY_CODE)
            OwnerHomeScreen(
                navController = navController,
                initialSpotifyCode = code
            )
        }
    }
}