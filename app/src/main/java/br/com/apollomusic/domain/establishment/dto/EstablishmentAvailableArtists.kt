package br.com.apollomusic.domain.establishment.dto

data class EstablishmentAvailableArtists(
    val genresAvailable: Set<String>,
    val totalVotes: Long
)
