package br.com.apollomusic.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object OwnerHome : Screen("owner-home")
}