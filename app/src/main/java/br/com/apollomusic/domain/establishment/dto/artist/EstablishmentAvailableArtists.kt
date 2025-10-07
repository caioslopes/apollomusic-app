package br.com.apollomusic.domain.establishment.dto.artist

data class EstablishmentAvailableArtists(
    val genresAvailable: Set<String>,
    val totalVotes: Long
)
