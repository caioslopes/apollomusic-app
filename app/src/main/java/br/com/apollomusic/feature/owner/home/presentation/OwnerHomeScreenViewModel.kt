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
import java.io.IOException
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
                popUpTo(Graph.Owner.route) { inclusive = true }
            }
        }
    }

    fun linkSpotify(code: String, navController: NavHostController) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val response = ownerRepository.sendSpotifyAuthorizationCode(code)

                if (response) {
                    _uiState.update { it.copy(isLoading = false, successMessage = "Spotify vinculado com sucesso!") }
                    navController.navigate(Screen.OwnerHome.route) {
                        popUpTo(Screen.OwnerHome.route) { inclusive = true }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Não foi possível vincular sua conta Spotify.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erro ao vincular Spotify.") }
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

    fun getEstablishment() = viewModelScope.launch {
        try {
            val response = establishmentRepository.getEstablishment()
            _uiState.update { it.copy(establishment = response) }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Erro ao buscar estabelecimento.") }
        }
    }

    fun getDevices() = viewModelScope.launch {
        try {
            val response = establishmentRepository.getDevice()
            _uiState.update { it.copy(devices = response.devices) }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Erro ao buscar dispositivos.") }
        }
    }

    fun getPlaylist() = viewModelScope.launch {
        try {
            val response = establishmentRepository.getPlaylist()
            _uiState.update { it.copy(playlist = response) }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Erro ao buscar playlist.") }
        }
    }

    fun createAndFetchPlaylist() = viewModelScope.launch {
        try {
            establishmentRepository.createPlaylist()
            val playlist = establishmentRepository.getPlaylist()
            _uiState.update { it.copy(playlist = playlist) }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Erro ao gerar playlist") }
        }
    }

    fun setDevice(deviceId: String) {
        viewModelScope.launch {
            try {
                establishmentRepository.setDevice(deviceId)
                _uiState.update { it.copy(successMessage = "Dispositivo atualizado com sucesso!") }
            } catch (e: IOException) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Erro ao definir dispositivo.") }
            }
        }
    }

    fun turnOn() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
                establishmentRepository.turnOn()
                _uiState.update { it.copy(isLoading = false, successMessage = "Estabelecimento ligado!") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao ligar o estabelecimento") }
            }
        }
    }

    fun turnOff() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
                establishmentRepository.turnOff()
                _uiState.update { it.copy(isLoading = false, successMessage = "Estabelecimento desligado!") }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Falha ao desligar o estabelecimento") }
            }
        }
    }

    fun toggleEstablishment() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

                val establishment = _uiState.value.establishment ?: return@launch

                if (establishment.isOff) {
                    establishmentRepository.turnOn()
                } else {
                    establishmentRepository.turnOff()
                }

                val updatedEstablishment = establishmentRepository.getEstablishment()
                val updatedPlaylist = establishmentRepository.getPlaylist()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        establishment = updatedEstablishment,
                        playlist = updatedPlaylist
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Falha ao alterar estado do estabelecimento")
                }
            }
        }
    }

    fun clearError() = _uiState.update { it.copy(errorMessage = null) }
    fun clearSuccess() = _uiState.update { it.copy(successMessage = null) }
}
