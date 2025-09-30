package br.com.apollomusic.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Welcome : Screen("welcome")

    data object UserLogin : Screen("user-login")
    data object UserHome : Screen("user-home")

    data object OwnerLogin : Screen("owner-login")
    data object OwnerHome : Screen("owner-home?spotifyCode={spotifyCode}") {
        const val ARG_SPOTIFY_CODE = "spotifyCode"

        fun createRoute(spotifyCode: String? = null): String {
            return if (!spotifyCode.isNullOrBlank()) {
                "owner-home?spotifyCode=$spotifyCode"
            } else {
                "owner-home"
            }
        }
    }
}