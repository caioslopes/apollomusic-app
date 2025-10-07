package br.com.apollomusic.feature.owner.config

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.owner.config.presentation.OwnerConfigScreenViewModel
import br.com.apollomusic.feature.owner.home.ui.components.PlaylistControl
import br.com.apollomusic.ui.components.ApolloArtistSearch
import br.com.apollomusic.ui.components.ApolloButton
import br.com.apollomusic.ui.components.ApolloCommonHeader
import br.com.apollomusic.ui.components.ApolloUserHeader
import br.com.apollomusic.ui.components.SelectionMode
import br.com.apollomusic.ui.theme.Grey80
import br.com.apollomusic.ui.theme.Grey90
import br.com.apollomusic.ui.theme.Rose

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerConfigScreen(
    navController: NavHostController,
    viewModel: OwnerConfigScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    if (state.isArtistDrawerOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeArtistDrawer() },
            sheetState = sheetState
        ) {

            ApolloArtistSearch(
                header = {
                    Text(
                        "Selecione os artistas da sua playlist",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                selectionMode = SelectionMode.MULTI,
                searchQuery = state.artistSearchQuery,
                onQueryChange = viewModel::onArtistSearchQueryChange,
                onSearchClick = viewModel::onSearchArtists,
                searchResult = state.artistSearchResult,
                selectedArtists = state.selectedArtists,
                onArtistSelect = viewModel::onArtistSelect,
                isSearching = state.isSearchingArtists,
            )
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding()) {
                ApolloCommonHeader(Modifier)
                ApolloUserHeader(
                    Modifier,
                    state.owner?.name ?: "",
                    onClickExit = { viewModel.onLogout(navController) },
                    state.owner?.hasThirdPartyAccess ?: false
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            PlaylistControl(
                name = state.playlist?.name ?: "",
                description = state.playlist?.description ?: "Lorem ipsum dolor sit amet.",
                onConfigClick = { viewModel.openArtistDrawer() }
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Grey90)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Artistas Selecionados:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(12.dp))

                if (state.selectedArtists.isEmpty()) {
                    Text("Nenhum artista selecionado ainda.")
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.selectedArtists.forEach { artist ->
                            ArtistTag(name = artist.name)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                ApolloButton(
                    text = "Finalizar",
                    onClick = {
                        val selectedIds = state.selectedArtists.map { it.id }.toSet()
                        viewModel.setInitialArtists(
                            artistIds = selectedIds,
                            onSuccess = { navController.popBackStack() }
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun ArtistTag(name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Grey80)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {


        Text(
            text = name,
            color = Rose,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}
