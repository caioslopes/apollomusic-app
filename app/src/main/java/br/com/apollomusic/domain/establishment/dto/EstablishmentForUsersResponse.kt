package br.com.apollomusic.domain.establishment.dto

data class EstablishmentForUsersResponse(
    val isOff: Boolean,
    val name: String,
    val totalUsers: Long
)
