package br.com.apollomusic.feature.owner.config.presentation

import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.domain.establishment.dto.playlist.PlaylistResponse
import br.com.apollomusic.domain.owner.dto.OwnerResponse

data class OwnerConfigUiState(
    val playlist: PlaylistResponse? = null,
    val artistSearchQuery: String = "",
    val artistSearchResult: List<Artist> = emptyList(),
    val selectedArtists: List<Artist> = emptyList(),
    val isArtistDrawerOpen: Boolean = false,
    val isSearchingArtists: Boolean = false,
    val owner: OwnerResponse? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
