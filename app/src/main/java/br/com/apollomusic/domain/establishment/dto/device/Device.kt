package br.com.apollomusic.domain.establishment.dto.device

data class Device(
    val id: String,
    val name: String,
    val type: DeviceType
)