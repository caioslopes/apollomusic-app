package br.com.apollomusic.feature.owner.login

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.owner.login.presentation.OwnerLoginScreenViewModel
import br.com.apollomusic.feature.owner.login.ui.components.OwnerLoginForm
import br.com.apollomusic.ui.UiEvent
import br.com.apollomusic.ui.components.ApolloWelcomeTemplate

@Composable
fun OwnerLoginScreen(
    onGoBack: () -> Unit,
    viewModel: OwnerLoginScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        ApolloWelcomeTemplate({
            OwnerLoginForm(
                state = state,
                onEstablishmentIdChange = viewModel::onEstablishmentIdChange,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onLoginClick = { viewModel.doLogin(navController) }
            )
        })
    }
}