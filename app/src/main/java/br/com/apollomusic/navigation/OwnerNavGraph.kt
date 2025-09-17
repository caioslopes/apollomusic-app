package br.com.apollomusic.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import br.com.apollomusic.feature.owner.home.OwnerHomeScreen

fun NavGraphBuilder.ownerNavGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.OwnerHome.route,
        route = Graph.Owner.route
    ) {
        composable(Screen.OwnerHome.route) {
            OwnerHomeScreen(
                onNavigateToDetail = { navController.navigate(Screen.Welcome.route) },
                navController = navController
            )
        }
    }
}