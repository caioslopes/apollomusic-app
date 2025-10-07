package br.com.apollomusic.feature.user.login.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.ui.components.ApolloButton

@Composable
fun Step3_SelectArtists(
    username: String,
    selectedArtists: List<Artist>,
    onOpenDrawer: () -> Unit,
    onLoginClick: () -> Unit,
    isLoading: Boolean
) {
    Column {
        Text("Usuário: $username")
        Text("Artistas selecionados: ${selectedArtists.joinToString { it.name }.ifEmpty { "Nenhum" }}")

        Spacer(Modifier.height(32.dp))

        ApolloButton(
            text = if (selectedArtists.isEmpty()) "Escolher meus artistas" else "Alterar artistas",
            onClick = onOpenDrawer
        )

        ApolloButton(
            text = "Acessar",
            onClick = onLoginClick,
            //enabled = selectedArtists.isNotEmpty() && !isLoading,
            //isLoading = isLoading
        )
    }
}