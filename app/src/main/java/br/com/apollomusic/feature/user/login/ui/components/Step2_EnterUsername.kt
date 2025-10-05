package br.com.apollomusic.feature.user.login.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.apollomusic.ui.components.ApolloButton
import br.com.apollomusic.ui.components.ApolloInputText

@Composable
fun Step2_EnterUsername(
    establishmentName: String?,
    username: String,
    onUsernameChange: (String) -> Unit,
    onNextClick: () -> Unit,
    errorMessage: String?
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Bem-vindo ao",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = establishmentName ?: "Estabelecimento",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        ApolloInputText(
            value = username,
            onValueChange = onUsernameChange,
            label = "Seu nome ou apelido",
            isError = errorMessage != null
        )

        if (errorMessage != null) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }

        ApolloButton(
            text = "Selecionar Artistas",
            onClick = onNextClick
        )
    }
}