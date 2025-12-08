package br.com.apollomusic.feature.user.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.domain.establishment.repository.EstablishmentRepository
import br.com.apollomusic.domain.role.UserRole
import br.com.apollomusic.domain.user.repository.UserRepository
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
class UserLoginScreenViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val establishmentRepository: EstablishmentRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserLoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEstablishmentIdChange(id: String) {
        _uiState.update {
            it.copy(establishmentId = id, errorMessage = null)
        }
    }

    fun onFindEstablishment() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val establishmentId = _uiState.value.establishmentId

            when (val result = establishmentRepository.getEstablishmentById(establishmentId)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            establishmentName = result.data?.name,
                            currentStep = LoginStep.ENTER_USERNAME
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendError(result.message)
                }
                is NetworkResult.Loading -> {}
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

            val establishmentId = try {
                _uiState.value.establishmentId.toLong()
            } catch (e: NumberFormatException) {
                _uiState.update { it.copy(isSearchingArtists = false) }
                sendError("ID do estabelecimento inválido")
                return@launch
            }

            val artistSearchQuery = _uiState.value.artistSearchQuery

            when (val result = establishmentRepository.userSearchForArtists(establishmentId, artistSearchQuery)) {
                is NetworkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSearchingArtists = false,
                            isLoading = false,
                            artistSearchResult = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSearchingArtists = false
                        )
                    }
                    sendError(result.message)
                }
                is NetworkResult.Loading -> {}
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
                viewModelScope.launch {
                    sendError("Você só pode selecionar até 3 artistas.")
                }
            }
        }
        _uiState.update { it.copy(selectedArtists = currentSelection) }
    }

    fun onLogin(navController: NavController) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val response = userRepository.login(
                username = _uiState.value.username,
                establishmentId = _uiState.value.establishmentId.toLong(),
                genres = _uiState.value.selectedArtists.map { it.id }
            )

            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        tokenManager.saveToken(it.accessToken)
                        tokenManager.saveUserRole(UserRole.USER)

                        navController.navigate(Graph.User.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendError(response.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    private suspend fun sendError(message: String?) {
        _uiEvent.send(UiEvent.ShowSnackbar(message ?: "Ocorreu um erro inesperado."))
    }
}
