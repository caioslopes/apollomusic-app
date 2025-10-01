package br.com.apollomusic.feature.owner.home.presentation

import br.com.apollomusic.domain.owner.dto.OwnerResponse

data class OwnerHomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val owner: OwnerResponse? = null
)