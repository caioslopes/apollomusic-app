package br.com.apollomusic.data.api

import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.device.Device
import br.com.apollomusic.domain.establishment.dto.device.DeviceResponse
import br.com.apollomusic.domain.establishment.dto.playlist.PlaylistResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface EstablishmentApiService {

    @GET("establishment")
    suspend fun getEstablishment(): EstablishmentResponse

    @GET("establishment/devices")
    suspend fun getDevices(): DeviceResponse

    @GET("establishment/playlist")
    suspend fun getPlaylist(): PlaylistResponse

    @POST("establishment/playlist")
    suspend fun createPlaylist(): Response<Unit>

}