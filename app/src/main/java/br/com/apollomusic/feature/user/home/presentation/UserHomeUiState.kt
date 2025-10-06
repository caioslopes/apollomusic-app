package br.com.apollomusic.feature.user.home.presentation

import br.com.apollomusic.domain.establishment.dto.EstablishmentForUsersResponse
import br.com.apollomusic.domain.user.User

data class EstablishmentInfo(
    val name: String,
    //val imageUrl: String,
    val activeUsers: Int
)

data class UserHomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val user: User? = null,
    val establishmentInfo: EstablishmentForUsersResponse? = null
)
