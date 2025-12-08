package br.com.apollomusic.feature.user.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.user.login.presentation.LoginStep
import br.com.apollomusic.feature.user.login.presentation.UserLoginScreenViewModel
import br.com.apollomusic.feature.user.login.ui.components.Step1_FindEstablishment
import br.com.apollomusic.feature.user.login.ui.components.Step2_EnterUsername
import br.com.apollomusic.feature.user.login.ui.components.Step3_SelectArtists
import br.com.apollomusic.ui.UiEvent
import br.com.apollomusic.ui.components.ApolloArtistSearch
import br.com.apollomusic.ui.components.ApolloWelcomeTemplate
import br.com.apollomusic.ui.components.SelectionMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginScreen(
    onGoBack: () -> Unit,
    viewModel: UserLoginScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            }
        }
    }

    if (state.isArtistDrawerOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeArtistDrawer() },
            sheetState = sheetState
        ) {
            ApolloArtistSearch(
                header = {
                    Text("Escolha seu artista favorito", style = MaterialTheme.typography.titleLarge)
                },
                selectionMode = SelectionMode.MULTI,
                searchQuery = state.artistSearchQuery,
                onQueryChange = viewModel::onArtistSearchQueryChange,
                onSearchClick = viewModel::onSearchArtists,
                searchResult = state.artistSearchResult,
                selectedArtists = state.selectedArtists,
                onArtistSelect = viewModel::onArtistSelect,
                isSearching = state.isSearchingArtists
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        ApolloWelcomeTemplate() {
            when (state.currentStep) {
                LoginStep.FIND_ESTABLISHMENT -> {
                    Step1_FindEstablishment(
                        establishmentId = state.establishmentId,
                        onIdChange = viewModel::onEstablishmentIdChange,
                        onFindClick = viewModel::onFindEstablishment,
                        isLoading = state.isLoading,
                        errorMessage = state.errorMessage
                    )
                }
                LoginStep.ENTER_USERNAME -> {
                    Step2_EnterUsername(
                        establishmentName = state.establishmentName,
                        username = state.username,
                        onUsernameChange = viewModel::onUsernameChange,
                        onNextClick = viewModel::onProceedToArtistSelection,
                        errorMessage = state.errorMessage
                    )
                }
                LoginStep.SELECT_ARTISTS -> {
                    Step3_SelectArtists(
                        username = state.username,
                        selectedArtists = state.selectedArtists,
                        onOpenDrawer = {
                            viewModel.openArtistDrawer()
                        },
                        onLoginClick = { viewModel.onLogin(navController) },
                        isLoading = state.isLoading
                    )
                }
            }
        }
    }
}
