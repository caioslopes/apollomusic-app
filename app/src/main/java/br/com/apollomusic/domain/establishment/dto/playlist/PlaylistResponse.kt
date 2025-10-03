package br.com.apollomusic.domain.establishment.dto.playlist

data class PlaylistResponse(
    val id: String,
    val name: String,
    val description: String,
    val images: List<Image>,
    val initialGenres: Map<String, Int>,
    val blockedGenres: Map<String, Int>,
    val genres: Map<String, Int>,
    val hasIncrementedGenre: Boolean
)