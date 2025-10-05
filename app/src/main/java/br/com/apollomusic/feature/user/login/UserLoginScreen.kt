package br.com.apollomusic.feature.user.login

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.apollomusic.feature.user.login.presentation.LoginStep
import br.com.apollomusic.feature.user.login.presentation.UserLoginScreenViewModel
import br.com.apollomusic.feature.user.login.ui.components.ArtistSelectionDrawerContent
import br.com.apollomusic.feature.user.login.ui.components.Step1_FindEstablishment
import br.com.apollomusic.feature.user.login.ui.components.Step2_EnterUsername
import br.com.apollomusic.feature.user.login.ui.components.Step3_SelectArtists
import br.com.apollomusic.ui.components.ApolloWelcomeTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserLoginScreen(
    onGoBack: () -> Unit,
    viewModel: UserLoginScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (state.isArtistDrawerOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.closeArtistDrawer() },
            sheetState = sheetState
        ) {
            ArtistSelectionDrawerContent(
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

    ApolloWelcomeTemplate {
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
