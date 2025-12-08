package br.com.apollomusic.feature.owner.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
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
class OwnerHomeScreenViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val ownerRepository: OwnerRepository,
    private val establishmentRepository: EstablishmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerHomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
            _uiState.update { it.copy(isLoading = true) }

            when (val result = ownerRepository.sendSpotifyAuthorizationCode(code)) {
                is NetworkResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    sendSuccess("Spotify vinculado com sucesso!")
                    navController.navigate(Screen.OwnerHome.route) {
                        popUpTo(Screen.OwnerHome.route) { inclusive = true }
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

    fun getEstablishment() = viewModelScope.launch {
        when (val result = establishmentRepository.getEstablishment()) {
            is NetworkResult.Success -> {
                _uiState.update { it.copy(establishment = result.data, isLoadingEstablishment = false) }
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingEstablishment = false) }
                sendError(result.message)
            }
            is NetworkResult.Loading -> {}
        }
    }

    fun getDevices() = viewModelScope.launch {
        when (val result = establishmentRepository.getDevice()) {
            is NetworkResult.Success -> {
                _uiState.update { it.copy(devices = result.data?.devices ?: emptyList(), isLoadingDevices = false) }
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingDevices = false) }
                sendError(result.message)
            }
            is NetworkResult.Loading -> {}
        }
    }

    fun getPlaylist() = viewModelScope.launch {
        when (val result = establishmentRepository.getPlaylist()) {
            is NetworkResult.Success -> {
                _uiState.update { it.copy(playlist = result.data, isLoadingPlaylist = false) }
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingPlaylist = false) }
                sendError(result.message)
            }
            is NetworkResult.Loading -> {}
        }
    }

    fun createAndFetchPlaylist() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        when (val createResult = establishmentRepository.createPlaylist()) {
            is NetworkResult.Success -> {
                // Fetch playlist updates isLoadingPlaylist which is fine
                when (val playlistResult = establishmentRepository.getPlaylist()) {
                    is NetworkResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, playlist = playlistResult.data, isLoadingPlaylist = false) }
                    }
                    is NetworkResult.Error -> {
                        _uiState.update { it.copy(isLoading = false, isLoadingPlaylist = false) }
                        sendError(playlistResult.message)
                    }
                    else -> {}
                }
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoading = false) }
                sendError(createResult.message)
            }
            else -> {}
        }
    }

    fun setDevice(deviceId: String) {
        viewModelScope.launch {
            when (val result = establishmentRepository.setDevice(deviceId)) {
                is NetworkResult.Success -> sendSuccess("Dispositivo atualizado com sucesso!")
                is NetworkResult.Error -> sendError(result.message)
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun turnOn() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingEstablishment = true) }
            when (val result = establishmentRepository.turnOn()) {
                is NetworkResult.Success -> {
                    sendSuccess("Estabelecimento ligado!")
                    getEstablishment()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingEstablishment = false) }
                    sendError(result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun turnOff() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingEstablishment = true) }
            when (val result = establishmentRepository.turnOff()) {
                is NetworkResult.Success -> {
                    sendSuccess("Estabelecimento desligado!")
                    getEstablishment()
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(isLoadingEstablishment = false) }
                    sendError(result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun toggleEstablishment() {
        viewModelScope.launch {
            val establishment = _uiState.value.establishment ?: return@launch
            if (establishment.isOff) {
                turnOn()
            } else {
                turnOff()
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
