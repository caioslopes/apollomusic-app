package br.com.apollomusic.domain.location.dto

data class VerifyLocationRequest(
    val deviceToken: String,
    val latitude: String,
    val longitude: String
)
