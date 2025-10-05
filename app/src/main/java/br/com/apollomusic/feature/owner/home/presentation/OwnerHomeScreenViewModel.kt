package br.com.apollomusic.feature.owner.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
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
class OwnerHomeScreenViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val ownerRepository: OwnerRepository,
    private val establishmentRepository: EstablishmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerHomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onLogout(navController: NavHostController) {
        viewModelScope.launch {
            tokenManager.clearSession()
            navController.navigate(Screen.Welcome.route) {
                popUpTo(Graph.Owner.route) {
                    inclusive = true
                }
            }
        }
    }

    fun linkSpotify(code: String, navController: NavHostController) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

                val response = ownerRepository.sendSpotifyAuthorizationCode(code)

                if (response) {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Spotify vinculado com sucesso!") }

                    navController.navigate(Screen.OwnerHome.route) {
                        popUpTo(Screen.OwnerHome.route) { inclusive = true }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Não foi possível vincular sua conta Spotify."
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível vincular sua conta Spotify."
                    )
                }
            }
        }
    }

    fun getOwner() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

                val response = ownerRepository.getOwner()

                _uiState.update { it.copy(owner = response) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível consultar usuário."
                    )
                }
            }
        }
    }

    fun getEstablishment() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

                val response = establishmentRepository.getEstablishment()

                _uiState.update { it.copy(establishment = response) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível consultar estabelecimento."
                    )
                }
            }
        }
    }

    fun getDevices() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

                val response = establishmentRepository.getDevice()

                _uiState.update { it.copy(devices = response.devices) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível consultar dispositivos."
                    )
                }
            }
        }
    }

    fun createPlaylist() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

                establishmentRepository.createPlaylist()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível criar playlist."
                    )
                }
            }
        }
    }

    fun getPlaylist() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
                val response = establishmentRepository.getPlaylist()

                _uiState.update { it.copy(playlist = response) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Não foi possível criar playlist."
                    )
                }
            }
        }
    }

    fun createAndFetchPlaylist() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                establishmentRepository.createPlaylist()
                val playlist = establishmentRepository.getPlaylist()

                _uiState.update { it.copy(isLoading = false, playlist = playlist) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao gerar playlist") }
            }
        }
    }


    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(successMessage = null) }
    }
}


