package br.com.apollomusic.feature.owner.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import br.com.apollomusic.domain.owner.repository.OwnerRepository
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerLoginScreenViewModel @Inject constructor(
    private val ownerRepository: OwnerRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerLoginUiState(isLoading = true))

    val uiState: StateFlow<OwnerLoginUiState> = _uiState

    fun handleForm(email: String = "", password: String = "", establishmentId: String = ""){
        _uiState.value = _uiState.value.copy(
            form = OwnerLoginForm(email, password, establishmentId),
            isLoading = false,
            errorMessage = null
        )
    }

    fun doLogin(navController: NavController){
        viewModelScope.launch {
            try{
                _uiState.value = _uiState.value.copy(isLoading = true, form = _uiState.value.form, errorMessage = null)

                val email = _uiState.value.form.email
                val password = _uiState.value.form.password
                val establishmentId = _uiState.value.form.establishmentId
                val response = ownerRepository.login(email, password, establishmentId)

                response.accessToken.let { token ->
                    tokenManager.saveToken(token)
                    navController.navigate(Screen.OwnerHome.route)
                }
            }catch(e: Exception){
                _uiState.value = _uiState.value.copy(isLoading = false, form = OwnerLoginForm(), errorMessage = "Falha ao fazer login")
            }
        }
    }

}