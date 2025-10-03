package br.com.apollomusic.domain.establishment.dto.device

import com.google.gson.annotations.SerializedName

enum class DeviceType {
    @SerializedName("Smartphone")
    SMARTPHONE,

    @SerializedName("Computer")
    COMPUTER,

    @SerializedName("Speaker")
    SPEAKER
}
