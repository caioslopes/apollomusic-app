package br.com.apollomusic.feature.user.login.presentation

import br.com.apollomusic.domain.establishment.dto.Artist

enum class LoginStep {
    FIND_ESTABLISHMENT,
    ENTER_USERNAME,
    SELECT_ARTISTS
}

data class UserLoginUiState(
    val currentStep: LoginStep = LoginStep.FIND_ESTABLISHMENT,

    val establishmentId: String = "",
    val establishmentName: String? = null,
    val username: String = "",

    val artistSearchQuery: String = "",
    val artistSearchResult: List<Artist> = emptyList(),
    val selectedArtists: List<Artist> = emptyList(),

    val isLoading: Boolean = false,
    val isSearchingArtists: Boolean = false,
    val errorMessage: String? = null,
    val isArtistDrawerOpen: Boolean = false
)