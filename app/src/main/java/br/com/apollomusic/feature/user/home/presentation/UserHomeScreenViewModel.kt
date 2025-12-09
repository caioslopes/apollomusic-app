package br.com.apollomusic.feature.user.home.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import br.com.apollomusic.domain.establishment.repository.EstablishmentRepository
import br.com.apollomusic.domain.user.User
import br.com.apollomusic.navigation.Graph
import br.com.apollomusic.navigation.Screen
import br.com.apollomusic.network.NetworkResult
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
                        establishmentInfo = establishmentData.data
                    )
                }
                
                getPosts(decodedJwt.establishmentId.toLong())

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

    fun getPosts(establishmentId: Long) = viewModelScope.launch {
        _uiState.update { it.copy(isLoadingFeed = true) }

        val result = establishmentRepository.getPosts(establishmentId)

        when (result) {
            is NetworkResult.Success -> {
                _uiState.update { it.copy(posts = result.data ?: emptyList(), isLoadingFeed = false) }
            }
            is NetworkResult.Error -> {
                _uiState.update { it.copy(isLoadingFeed = false, errorMessage = result.message) }
            }

            is NetworkResult.Loading<*> -> TODO()
        }
    }

    fun onContentChange(content: String) {
        _uiState.update { it.copy(newPostContent = content) }
    }

    fun onImageCaptured(uri: Uri?) {
        _uiState.update { it.copy(newPostImageUri = uri) }
    }

    fun onShowCreatePostSheet(show: Boolean) {
        if (!show) {
            _uiState.update { it.copy(showCreatePostSheet = false, newPostContent = "", newPostImageUri = null) }
        } else {
            _uiState.update { it.copy(showCreatePostSheet = true) }
        }
    }

    fun createPost(context: Context) = viewModelScope.launch {
        _uiState.update { it.copy(isCreatingPost = true) }

        try {
            val currentState = _uiState.value
            val uri = currentState.newPostImageUri ?: throw IllegalStateException("A imagem não pode ser nula.")
            val username = currentState.user?.username ?: "Usuário Anônimo"
            val establishmentId = currentState.user?.establishmentId ?: throw IllegalStateException("ID do estabelecimento não encontrado.")

            val result = establishmentRepository.createPost(
                context = context,
                username = username,
                content = currentState.newPostContent,
                establishmentId = establishmentId,
                imageUris = listOf(uri)
            )

            when (result) {
                is NetworkResult.Success -> {
                    onShowCreatePostSheet(false)
                    getPosts(establishmentId)
                }
                is NetworkResult.Error -> {
                    _uiState.update { it.copy(errorMessage = result.message) }
                }
                else -> {
                }
            }

        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.message) }
        } finally {
            _uiState.update { it.copy(isCreatingPost = false) }
        }
    }


}