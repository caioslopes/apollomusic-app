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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.apollomusic.feature.splash.presentation.SplashViewModel
import br.com.apollomusic.navigation.AppNavGraph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.services.LocationTrackingService
import br.com.apollomusic.ui.theme.ApolloMusicTheme
import br.com.apollomusic.utils.TokenManager
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager
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
                    Log.d("FCM_APOLLO", "Token Obtido na MainActivity: $token")

                    lifecycleScope.launch {
                        tokenManager.saveFCMToken(token)
                        Log.d("FCM_APOLLO", "Token salvo com sucesso no DataStore.")
                    }
                } else {
                    Log.w("FCM_APOLLO", "Falha ao obter o token FCM", task.exception)
                }
            }

        checkAndRequestPermissions()

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

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Log.d("Permissions", "${it.key} = ${it.value}")
        }

        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted) {
            startLocationService()
        } else {
            Log.w("Permissions", "Permissão de localização fina não concedida.")
        }
    }

    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        val hasFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocation || !hasCoarseLocation) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasNotificationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasNotificationPermission) {
                permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            Log.d("Permissions", "Solicitando permissões: $permissionsToRequest")
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            Log.d("Permissions", "Todas as permissões necessárias já foram concedidas.")
            startLocationService()
        }
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
