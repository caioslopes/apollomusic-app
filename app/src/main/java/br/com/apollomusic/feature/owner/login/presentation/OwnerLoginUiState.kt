package br.com.apollomusic.feature.owner.login.presentation

data class OwnerLoginForm(
    val email: String = "",
    val password: String = "",
    val establishmentId: String = "",
)

data class OwnerLoginUiState(
    val form: OwnerLoginForm = OwnerLoginForm(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
