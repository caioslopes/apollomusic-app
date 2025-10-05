package br.com.apollomusic.feature.user.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.apollomusic.domain.establishment.dto.Artist
import br.com.apollomusic.domain.establishment.repository.EstablishmentRepository
import br.com.apollomusic.domain.role.UserRole
import br.com.apollomusic.domain.user.repository.UserRepository
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
class UserLoginScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val establishmentRepository: EstablishmentRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserLoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEstablishmentIdChange(id: String) {
        _uiState.update {
            it.copy(establishmentId = id, errorMessage = null)
        }
    }

    fun onFindEstablishment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val establishmentId = _uiState.value.establishmentId
                val response = establishmentRepository.getEstablishmentById(establishmentId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        establishmentName = response.name,
                        currentStep = LoginStep.ENTER_USERNAME
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Ocorreu um erro."
                    )
                }
            }
        }
    }

    fun onUsernameChange(name: String) {
        _uiState.update {
            it.copy(username = name, errorMessage = null)
        }
    }

    fun onProceedToArtistSelection() {
        if (_uiState.value.username.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Por favor, insira um nome de usuário.")
            }
            return
        }

        _uiState.update {
            it.copy(
                errorMessage = null,
                currentStep = LoginStep.SELECT_ARTISTS
            )
        }
    }

    fun onArtistSearchQueryChange(query: String) {
        _uiState.update { it.copy(artistSearchQuery = query) }
    }

    fun openArtistDrawer() {
        _uiState.update { it.copy(isArtistDrawerOpen = true) }
    }

    fun closeArtistDrawer() {
        _uiState.update { it.copy(isArtistDrawerOpen = false) }
    }

    fun onSearchArtists() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSearchingArtists = true, artistSearchResult = emptyList()) }

            try {
                val establishmentId = _uiState.value.establishmentId.toLong()
                val artistSearchQuery = _uiState.value.artistSearchQuery
                val response = establishmentRepository.userSearchForArtists(establishmentId, artistSearchQuery)
                _uiState.update {
                    it.copy(
                        isSearchingArtists = false,
                        isLoading = false,
                        artistSearchResult = response
                    )
                }
            }catch (e: Exception){
                _uiState.update { it.copy(isLoading = false, isSearchingArtists = false, errorMessage = e.message) }
            }

        }
    }

    fun onArtistSelect(artist: Artist) {
        val currentSelection = _uiState.value.selectedArtists.toMutableList()
        val isAlreadySelected = currentSelection.contains(artist)

        if (isAlreadySelected) {
            currentSelection.remove(artist)
        } else {
            if (currentSelection.size < 3) {
                currentSelection.add(artist)
            } else {
                // Opcional: mostrar um Toast/Snackbar informando o limite
            }
        }
        _uiState.update { it.copy(selectedArtists = currentSelection) }
    }

    fun onLogin(navController: NavController) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                 val response = userRepository.login(
                     username = _uiState.value.username,
                     establishmentId = _uiState.value.establishmentId.toLong(),
                     genres = _uiState.value.selectedArtists.map { it.id }
                 )

                tokenManager.saveToken(response.accessToken)
                tokenManager.saveUserRole(UserRole.USER)

                navController.navigate(Graph.User.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Falha no login.") }
            }
        }
    }

}