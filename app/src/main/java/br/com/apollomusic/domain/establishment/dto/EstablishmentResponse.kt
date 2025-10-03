package br.com.apollomusic.domain.establishment.dto

import br.com.apollomusic.domain.establishment.dto.playlist.PlaylistResponse

data class EstablishmentResponse(
    val id: Long,
    val name: String,
    val deviceId: String,
    val isOff: Boolean,
    val playlist: PlaylistResponse
)




