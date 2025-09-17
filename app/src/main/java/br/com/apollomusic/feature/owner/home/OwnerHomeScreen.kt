package br.com.apollomusic.feature.owner.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.apollomusic.ui.components.ApolloCommonHeader
import br.com.apollomusic.ui.components.ApolloUserHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerHomeScreen(
    onNavigateToDetail: () -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                ApolloCommonHeader(Modifier)
                ApolloUserHeader(Modifier, "João Silva")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onNavigateToDetail,
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Ir para login")
            }
        }
    }
}
