package br.com.apollomusic.feature.owner.login.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import br.com.apollomusic.feature.owner.login.presentation.OwnerLoginUiState
import br.com.apollomusic.ui.components.ApolloButton
import br.com.apollomusic.ui.components.ApolloInputText

@Composable
fun OwnerLoginForm(
    state: OwnerLoginUiState,
    onEstablishmentIdChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
){
    ApolloInputText(
        value = state.form.establishmentId,
        onValueChange = onEstablishmentIdChange,
        label = "Código do estabelecimento",
        isError = state.form.establishmentIdError != null,
        supportingText = state.form.establishmentIdError
    )
    ApolloInputText(
        value = state.form.email,
        onValueChange = onEmailChange,
        label = "Email",
        isError = state.form.emailError != null,
        supportingText = state.form.emailError
    )
    ApolloInputText(
        value = state.form.password,
        onValueChange = onPasswordChange,
        label = "Senha",
        isError = state.form.passwordError != null,
        supportingText = state.form.passwordError
    )
    ApolloButton(
        text = "Acessar",
        onClick = onLoginClick,
        icon = Icons.AutoMirrored.Filled.ArrowForward,
        iconPosition = "right"
    )
}