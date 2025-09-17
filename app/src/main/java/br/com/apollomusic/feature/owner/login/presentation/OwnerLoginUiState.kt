package br.com.apollomusic.feature.owner.login.presentation

data class OwnerLoginForm(
    val email: String = "",
    val emailError: String? = null,

    val password: String = "",
    val passwordError: String? = null,

    val establishmentId: String = "",
    val establishmentIdError: String? = null
)

data class OwnerLoginUiState(
    val form: OwnerLoginForm = OwnerLoginForm(),

    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
