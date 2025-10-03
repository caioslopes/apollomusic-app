package br.com.apollomusic.data.api

import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.device.Device
import br.com.apollomusic.domain.establishment.dto.device.DeviceResponse
import retrofit2.http.GET

interface EstablishmentApiService {

    @GET("establishment")
    suspend fun getEstablishment(): EstablishmentResponse

    @GET("establishment/devices")
    suspend fun getDevices(): DeviceResponse
}