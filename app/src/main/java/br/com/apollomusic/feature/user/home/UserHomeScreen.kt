package br.com.apollomusic.feature.user.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.user.home.presentation.UserHomeScreenViewModel
import br.com.apollomusic.feature.user.home.ui.components.EstablishmentInfoCard
import br.com.apollomusic.feature.user.home.ui.components.EstablishmentInfoSkeleton
import br.com.apollomusic.ui.components.ApolloCommonHeader
import br.com.apollomusic.ui.components.ApolloUserHeader

@Composable
fun UserHomeScreen(
    navController: NavHostController,
    viewModel: UserHomeScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                ApolloCommonHeader(Modifier)
                ApolloUserHeader(
                    Modifier,
                    uiState.user?.username ?: "",
                    onClickExit = { viewModel.onLogout(navController) },
                    false
                )
            }
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) {
                EstablishmentInfoSkeleton()
            } else if (uiState.establishmentInfo != null) {
                EstablishmentInfoCard(info = uiState.establishmentInfo!!)
            } else if (uiState.errorMessage != null) {
                Text(text = "Erro ao carregar: ${uiState.errorMessage}")
            }
        }
    }
}
