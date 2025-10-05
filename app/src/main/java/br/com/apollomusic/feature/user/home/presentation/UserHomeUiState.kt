package br.com.apollomusic.feature.user.home.presentation

import br.com.apollomusic.domain.user.User

data class UserHomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: User? = null,
)
