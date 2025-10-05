package br.com.apollomusic.feature.user.login.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.com.apollomusic.ui.components.ApolloButton
import br.com.apollomusic.ui.components.ApolloInputText

@Composable
fun Step1_FindEstablishment(
    establishmentId: String,
    onIdChange: (String) -> Unit,
    onFindClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    ApolloInputText(
        value = establishmentId,
        onValueChange = onIdChange,
        label = "Código do Estabelecimento",
        isError = errorMessage != null,
        supportingText = errorMessage
    )

    ApolloButton(
        text = "Buscar Estabelecimento",
        onClick = onFindClick,
        //isLoading = isLoading // O botão pode mostrar um ícone de loading
    )
}