package br.com.apollomusic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.apollomusic.navigation.AppNavGraph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.ui.theme.ApolloMusicTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val pendingNavigation = MutableStateFlow<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ApolloMusicTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    navController = rememberNavController()
                    AppNavGraph(navController = navController)

                    LaunchedEffect(Unit) {
                        pendingNavigation.collect { code ->
                            code?.let {
                                navigateToOwnerHome(it)
                                pendingNavigation.value = null
                            }
                        }
                    }
                }
            }
        }

        handleSpotifyCallback(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleSpotifyCallback(intent)
    }

    private fun handleSpotifyCallback(intent: Intent) {
        intent.data?.let { uri ->
            if (uri.scheme == "apollomusic" && uri.host == "callback") {
                val code = uri.getQueryParameter("code")
                code?.let { spotifyCode ->
                    pendingNavigation.value = spotifyCode
                }
            }
        }
    }

    private fun navigateToOwnerHome(spotifyCode: String) {
        val route = Screen.OwnerHome.createRoute(spotifyCode)
        navController.navigate(route) {
            launchSingleTop = true
        }
    }
}
