package br.com.apollomusic.feature.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.R
import br.com.apollomusic.feature.splash.presentation.SplashViewModel
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.ui.theme.Rose

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Rose),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.adaptive_icon),
            contentDescription = "Logo do Apollo Music",
            modifier = Modifier.size(128.dp)
        )
    }
}