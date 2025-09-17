package br.com.apollomusic.feature.splash.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.apollomusic.domain.role.UserRole
import br.com.apollomusic.navigation.Graph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _startDestination = mutableStateOf("")
    val startDestination: State<String> = _startDestination

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            val token = tokenManager.getToken().first()
            val role = tokenManager.getUserRole().first()

            if (token.isNullOrBlank()) {
                _startDestination.value = Screen.Welcome.route
            } else {
                _startDestination.value = when (role) {
                    UserRole.OWNER -> Graph.Owner.route
                    UserRole.USER -> Graph.User.route
                    null -> Screen.Welcome.route
                }
            }
        }
    }
}