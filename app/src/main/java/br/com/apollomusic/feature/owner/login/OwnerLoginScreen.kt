package br.com.apollomusic.feature.owner.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.owner.login.presentation.OwnerLoginScreenViewModel
import br.com.apollomusic.ui.components.ApolloButton
import br.com.apollomusic.ui.components.ApolloInputText
import br.com.apollomusic.ui.components.ApolloWelcomeTemplate

@Composable
fun OwnerLoginScreen(
    onGoBack: () -> Unit,
    viewModel: OwnerLoginScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()

    ApolloWelcomeTemplate {
        ApolloInputText(
            value = state.form.establishmentId,
            onValueChange = { viewModel.handleForm(establishmentId = it, email = state.form.email, password = state.form.password) },
            label = "Código do estabelecimento"
        )
        ApolloInputText(
            value = state.form.email,
            onValueChange = { viewModel.handleForm(email = it, password = state.form.password) },
            label = "Email"
        )
        ApolloInputText(
            value = state.form.password,
            onValueChange = { viewModel.handleForm(email = state.form.email, password = it) },
            label = "Senha"
        )
        ApolloButton(
            text = "Acessar",
            onClick = { viewModel.doLogin(navController) },
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            iconPosition = "right"
        )
    }
}
