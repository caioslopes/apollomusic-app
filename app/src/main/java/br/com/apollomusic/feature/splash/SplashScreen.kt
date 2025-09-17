package br.com.apollomusic.feature.splash

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.splash.presentation.SplashViewModel
import br.com.apollomusic.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val startDestination = viewModel.startDestination.value

    LaunchedEffect(key1 = startDestination) {
        if (startDestination.isNotEmpty()) {
            navController.navigate(startDestination) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
            }
        }
    }

    CircularProgressIndicator()
}