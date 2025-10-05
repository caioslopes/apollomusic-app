package br.com.apollomusic.feature.user.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import br.com.apollomusic.domain.establishment.repository.EstablishmentRepository
import br.com.apollomusic.domain.user.repository.UserRepository
import br.com.apollomusic.navigation.Graph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeScreenViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val userRepository: UserRepository,
    private val establishmentRepository: EstablishmentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserHomeUiState())
    val uiState = _uiState.asStateFlow()

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