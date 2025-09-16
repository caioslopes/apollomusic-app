package br.com.apollomusic.domain.owner.dto

data class LoginRequest(
    val email: String,
    val password: String,
    val establishmentId: String
)
