package br.com.apollomusic.feature.owner.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.owner.home.presentation.OwnerHomeScreenViewModel
import br.com.apollomusic.ui.components.ApolloButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerHomeScreen(
    onNavigateToDetail: () -> Unit,
    navController: NavHostController,
    viewModel: OwnerHomeScreenViewModel = hiltViewModel(),
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Página inicial Owner") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            ApolloButton(
                text = "Sair",
                onClick = { viewModel.onLogout(navController) }
            )
        }
    }
}
