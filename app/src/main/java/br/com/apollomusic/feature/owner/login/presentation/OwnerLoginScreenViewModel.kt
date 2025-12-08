package br.com.apollomusic.feature.owner.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.apollomusic.domain.owner.repository.OwnerRepository
import br.com.apollomusic.domain.role.UserRole
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
class OwnerLoginScreenViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerLoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(email = email, emailError = null)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(password = password, passwordError = null)
            )
        }
    }

    fun onEstablishmentIdChange(establishmentId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(establishmentId = establishmentId, establishmentIdError = null)
            )
        }
    }

    fun doLogin(navController: NavController) {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val form = _uiState.value.form

            when (val result = ownerRepository.login(form.email, form.password, form.establishmentId)) {
                is NetworkResult.Success -> {
                    result.data?.accessToken?.let { token ->
                        tokenManager.saveToken(token)
                        tokenManager.saveUserRole(UserRole.OWNER)

                        _uiState.update { it.copy(isLoading = false) }

                        navController.navigate(Graph.Owner.route) {
                            popUpTo(Screen.Welcome.route) { inclusive = true }
                        }
                    } ?: run {
                        _uiState.update { it.copy(isLoading = false) }
                        sendError("Erro: Token não recebido.")
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

    private fun validateForm(): Boolean {
        val form = _uiState.value.form
        val hasError = form.email.isBlank() || form.password.isBlank() || form.establishmentId.isBlank()

        if (hasError) {
            _uiState.update {
                it.copy(
                    form = it.form.copy(
                        emailError = if (form.email.isBlank()) "Email não pode ser vazio" else null,
                        passwordError = if (form.password.isBlank()) "Senha não pode ser vazio." else null,
                        establishmentIdError = if (form.establishmentId.isBlank()) "Código não pode ser vazio" else null
                    )
                )
            }
        }

        return !hasError
    }

    private suspend fun sendError(message: String?) {
        _uiEvent.send(UiEvent.ShowSnackbar(message ?: "Ocorreu um erro inesperado."))
    }
}