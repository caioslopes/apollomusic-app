package br.com.apollomusic.domain.user

data class User(
    val artists: List<String>,
    val establishmentId: Long,
    val username: String,
)
