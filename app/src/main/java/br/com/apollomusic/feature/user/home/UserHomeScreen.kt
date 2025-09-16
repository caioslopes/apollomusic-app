package br.com.apollomusic.feature.user.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserHomeScreen(
    onNavigateToDetail: () -> Unit
) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Página inicial (Usuário)")

                Button(
                    onClick = onNavigateToDetail,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Ir para login")
                }
            }

        }
    }
}
