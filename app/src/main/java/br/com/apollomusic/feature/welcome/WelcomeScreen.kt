package br.com.apollomusic.feature.welcome

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import br.com.apollomusic.ui.components.ApolloButton
import br.com.apollomusic.ui.components.ApolloWelcomeTemplate

@Composable
fun WelcomeScreen(
    onNavigateToOwnerLogin : () -> Unit,
    onNavigateToUserLogin : () -> Unit
) {
    ApolloWelcomeTemplate {
        ApolloButton(
            text = "Usuário",
            onClick = onNavigateToUserLogin,
            icon = Icons.Default.Person
        )
        ApolloButton(
            text = "Proprietário",
            onClick = onNavigateToOwnerLogin,
            icon = Icons.Default.Lock
        )
    }
}

