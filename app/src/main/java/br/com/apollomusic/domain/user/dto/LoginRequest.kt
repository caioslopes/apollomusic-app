package br.com.apollomusic.domain.user.dto

data class LoginRequest(
    val username: String,
    val genres: List<String>,
    val establishmentId: Long
)