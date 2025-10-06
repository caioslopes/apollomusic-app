package br.com.apollomusic.feature.user.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import br.com.apollomusic.domain.establishment.repository.EstablishmentRepository
import br.com.apollomusic.domain.user.User
import br.com.apollomusic.navigation.Graph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.TokenManager
import br.com.apollomusic.utils.JwtUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeScreenViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val establishmentRepository: EstablishmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserHomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val token = tokenManager.getToken().first()
                if (token.isNullOrBlank()) { throw Exception("Token não encontrado.") }

                val decodedJwt = JwtUtils.decode(token)
                if (decodedJwt == null) { throw Exception("Token inválido.") }

                _uiState.update { it.copy(user = User(username = decodedJwt.username, establishmentId = decodedJwt.establishmentId.toLong(), artists = decodedJwt.artists)) }

                val establishmentData = establishmentRepository.getEstablishmentById(decodedJwt.establishmentId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        establishmentInfo = establishmentData
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onLogout(navController: NavHostController) {
        viewModelScope.launch {
            tokenManager.clearSession()
            navController.navigate(Screen.Welcome.route) {
                popUpTo(Graph.User.route) {
                    inclusive = true
                }
            }
        }
    }

}