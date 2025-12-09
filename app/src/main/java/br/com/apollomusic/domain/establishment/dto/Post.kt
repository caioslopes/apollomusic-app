package br.com.apollomusic.domain.establishment.dto

data class Post(
    val id: Long,
    val username: String,
    val content: String,
    val imageUrls: List<String>,
    val establishmentId: Long
)