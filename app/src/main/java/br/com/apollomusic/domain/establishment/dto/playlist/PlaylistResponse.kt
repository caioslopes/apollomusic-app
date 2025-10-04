package br.com.apollomusic.domain.establishment.dto.playlist

data class PlaylistResponse(
    val id: String,
    val name: String,
    val description: String,
    val images: Collection<Image>,
    val initialArtists: Map<String, Int>,
    val blockedArtists: Map<String, Int>,
    val artists: Map<String, Int>,
    val hasIncrementedArtist: Boolean
)