package br.com.apollomusic.feature.owner.config.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import br.com.apollomusic.domain.establishment.dto.artist.InitialArtistsRequest
import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.domain.establishment.repository.EstablishmentRepository
import br.com.apollomusic.domain.owner.repository.OwnerRepository
import br.com.apollomusic.navigation.Graph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            try {
                val query = _uiState.value.artistSearchQuery
                val response: List<Artist> = establishmentRepository.searchForArtists(query)

                _uiState.update {
                    it.copy(
                        isSearchingArtists = false,
                        artistSearchResult = response
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSearchingArtists = false, errorMessage = e.message) }
                println("Erro ao buscar artistas: ${e.message}")
            }
        }
    }

    fun setInitialArtists(artistIds: Set<String>, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
                establishmentRepository.setInitialArtists(InitialArtistsRequest(artistIds))
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Artistas iniciais definidos com sucesso!"
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao definir artistas") }
            }
        }
    }

    fun getOwner() = viewModelScope.launch {
        try {
            val response = ownerRepository.getOwner()
            _uiState.update { it.copy(owner = response) }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Erro ao buscar usuário.") }
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

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
    fun clearSuccess() = _uiState.update { it.copy(successMessage = null) }
}
