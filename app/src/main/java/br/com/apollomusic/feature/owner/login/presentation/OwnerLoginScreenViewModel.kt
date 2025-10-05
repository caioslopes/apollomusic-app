package br.com.apollomusic.feature.owner.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.apollomusic.domain.owner.repository.OwnerRepository
import br.com.apollomusic.domain.role.UserRole
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
class OwnerLoginScreenViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerLoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(email = email)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(password = password)
            )
        }
    }

    fun onEstablishmentIdChange(establishmentId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                form = currentState.form.copy(establishmentId = establishmentId)
            )
        }
    }

    fun doLogin(navController: NavController) {
        if (!validateForm()) {
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val form = _uiState.value.form
                val response = ownerRepository.login(form.email, form.password, form.establishmentId)

                response.accessToken.let { token ->
                    tokenManager.saveToken(token)
                    tokenManager.saveUserRole(UserRole.OWNER)

                    navController.navigate(Graph.Owner.route) {
                        popUpTo(Screen.Welcome.route) {
                            inclusive = true
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Falha ao fazer login. Verifique seus dados."
                    )
                }
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
}