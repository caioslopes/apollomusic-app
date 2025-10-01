package br.com.apollomusic.feature.owner.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.owner.home.presentation.OwnerHomeScreenViewModel
import br.com.apollomusic.feature.owner.home.ui.components.EstablishmentControl
import br.com.apollomusic.ui.components.ApolloCommonHeader
import br.com.apollomusic.ui.components.ApolloUserHeader
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerHomeScreen(
    navController: NavHostController,
    initialSpotifyCode: String? = null,
    viewModel: OwnerHomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    viewModel.getOwner()

    LaunchedEffect(initialSpotifyCode) {
        if (!initialSpotifyCode.isNullOrBlank() && initialSpotifyCode != "{spotifyCode}") {
            viewModel.linkSpotify(initialSpotifyCode, navController)
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                ApolloCommonHeader(Modifier)
                ApolloUserHeader(
                    Modifier,
                    uiState.owner?.name ?: "",
                    onClickExit = { viewModel.onLogout(navController) },
                    uiState.owner?.hasThirdPartyAccess ?: false
                )
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EstablishmentControl(
                    name = "Zinho",
                    statusText = "Ligado",
                    configured = false,
                    enabled = false
                )

                EstablishmentControl(
                    name = "Zinho",
                    statusText = "Desligado",
                    configured = true,
                    enabled = false
                )

                EstablishmentControl(
                    name = "Zinho",
                    statusText = "Tocando agora",
                    configured = true,
                    enabled = true
                )
            }

        }
    }
}
