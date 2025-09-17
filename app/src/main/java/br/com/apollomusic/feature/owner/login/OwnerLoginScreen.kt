package br.com.apollomusic.feature.owner.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.owner.login.presentation.OwnerLoginScreenViewModel
import br.com.apollomusic.feature.owner.login.ui.components.OwnerLoginForm
import br.com.apollomusic.ui.components.ApolloWelcomeTemplate

@Composable
fun OwnerLoginScreen(
    onGoBack: () -> Unit,
    viewModel: OwnerLoginScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()

    ApolloWelcomeTemplate {
        OwnerLoginForm(
            state = state,
            onEstablishmentIdChange = viewModel::onEstablishmentIdChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = { viewModel.doLogin(navController) }
        )
    }
}
