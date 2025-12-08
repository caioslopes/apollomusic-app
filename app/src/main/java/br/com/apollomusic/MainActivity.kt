package br.com.apollomusic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.apollomusic.feature.splash.presentation.SplashViewModel
import br.com.apollomusic.navigation.AppNavGraph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.services.LocationTrackingService
import br.com.apollomusic.ui.theme.ApolloMusicTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private val pendingNavigation = MutableStateFlow<String?>(null)

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.isLoading.value
        }

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

        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("FCM_APOLLO", "Token Obtido Manualmente: $token")
                }
            }

        checkAndRequestLocationPermissions()

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

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

        val backgroundLocationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions[Manifest.permission.ACCESS_BACKGROUND_LOCATION] == true
        } else {
            true
        }

        if (fineLocationGranted && backgroundLocationGranted) {
            startLocationService()
        } else {
        }
    }

    fun checkAndRequestLocationPermissions() {
        val fineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocation) {
            startLocationService()
            return
        }

        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionsToRequest.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        locationPermissionLauncher.launch(permissionsToRequest.toTypedArray())
    }

    fun startLocationService() {
        val intent = Intent(this, LocationTrackingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }
}
