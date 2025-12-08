package br.com.apollomusic.feature.owner.config.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.domain.establishment.dto.artist.InitialArtistsRequest
import br.com.apollomusic.domain.establishment.repository.EstablishmentRepository
import br.com.apollomusic.domain.owner.repository.OwnerRepository
import br.com.apollomusic.navigation.Graph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.NetworkResult
import br.com.apollomusic.network.TokenManager
import br.com.apollomusic.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerConfigScreenViewModel @Inject constructor(
    private val establishmentRepository: EstablishmentRepository,
    private val ownerRepository: OwnerRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerConfigUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onArtistSearchQueryChange(query: String) {
        _uiState.update { it.copy(artistSearchQuery = query) }
    }

    fun openArtistDrawer() {
        _uiState.update { it.copy(isArtistDrawerOpen = true) }
    }

    fun closeArtistDrawer() {
        _uiState.update { it.copy(isArtistDrawerOpen = false) }
    }

    fun onArtistSelect(artist: Artist) {
        val current = _uiState.value.selectedArtists.toMutableList()
        if (current.contains(artist)) current.remove(artist)
        else current.add(artist)
        _uiState.update { it.copy(selectedArtists = current) }
    }

    fun onSearchArtists() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingArtists = true, artistSearchResult = emptyList()) }
            val query = _uiState.value.artistSearchQuery

            when (val result = establishmentRepository.searchForArtists(query)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSearchingArtists = false,
                            artistSearchResult = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isSearchingArtists = false) }
                    sendError(result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun setInitialArtists(artistIds: Set<String>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val request = InitialArtistsRequest(artistIds)
            when (val result = establishmentRepository.setInitialArtists(request)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSuccess("Artistas iniciais definidos com sucesso!")
                    onSuccess()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendError(result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun getOwner() = viewModelScope.launch {
        when (val result = ownerRepository.getOwner()) {
            is NetworkResult.Success -> {
                _uiState.update { it.copy(owner = result.data, isLoadingOwner = false) }
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingOwner = false) }
                sendError(result.message)
            }
            is NetworkResult.Loading -> {}
        }
    }

    fun getPlaylist() = viewModelScope.launch {
        when (val result = establishmentRepository.getPlaylist()) {
            is NetworkResult.Success -> {
                val initialArtists = result.data?.initialArtists?.map { (key, _) ->
                    Artist(id = key, name = key, images = emptyList())
                } ?: emptyList()

                _uiState.update {
                    it.copy(
                        playlist = result.data,
                        isLoadingPlaylist = false,
                        selectedArtists = initialArtists
                    )
                }
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingPlaylist = false) }
                sendError(result.message)
            }
            is NetworkResult.Loading -> {}
        }
    }

    fun onLogout(navController: NavHostController) {
        viewModelScope.launch {
            tokenManager.clearSession()
            navController.navigate(Screen.Welcome.route) {
                popUpTo(Graph.Owner.route) { inclusive = true }
            }
        }
    }

    private suspend fun sendError(message: String?) {
        _uiEvent.send(UiEvent.ShowSnackbar(message ?: "Ocorreu um erro inesperado."))
    }

    private suspend fun sendSuccess(message: String) {
        _uiEvent.send(UiEvent.ShowSnackbar(message))
    }
}
