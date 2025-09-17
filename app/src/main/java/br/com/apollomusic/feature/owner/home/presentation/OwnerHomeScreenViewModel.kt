package br.com.apollomusic.feature.owner.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerHomeScreenViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    fun onLogout(navController: NavHostController) {
        viewModelScope.launch {
            tokenManager.clearSession()

            navController.navigate(Screen.Welcome.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

}