package br.com.apollomusic.domain.owner.dto

data class OwnerResponse(
    val name: String,
    val email: String,
    val hasThirdPartyAccess: Boolean
)