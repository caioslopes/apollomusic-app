package br.com.apollomusic.data.api

import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.domain.establishment.dto.artist.ArtistsResponse
import br.com.apollomusic.domain.establishment.dto.artist.EstablishmentAvailableArtists
import br.com.apollomusic.domain.establishment.dto.EstablishmentForUsersResponse
import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.artist.InitialArtistsRequest
import br.com.apollomusic.domain.establishment.dto.device.DeviceRequest
import br.com.apollomusic.domain.establishment.dto.device.DeviceResponse
import br.com.apollomusic.domain.establishment.dto.playlist.PlaylistResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EstablishmentApiService {

    @GET("establishment")
    suspend fun getEstablishment(): EstablishmentResponse

    @GET("establishment/{id}")
    suspend fun getEstablishment(@Path("id") id: String): EstablishmentForUsersResponse

    @GET("establishment/devices")
    suspend fun getDevices(): DeviceResponse

    @GET("establishment/playlist")
    suspend fun getPlaylist(): PlaylistResponse

    @POST("establishment/playlist")
    suspend fun createPlaylist(): Response<Unit>

    @GET("establishment/playlist/search-artists")
    suspend fun searchForArtists(@Query("query") query: String): List<Artist>

    @GET("establishment/playlist/for-user/search-artists")
    suspend fun userSearchForArtists(@Query("establishmentId") establishmentId: Long, @Query("query") query: String): List<Artist>

    @GET("establishment/playlist/genres/{id}")
    suspend fun getEstablishmentAvailableArtists(@Path("id") id: String): EstablishmentAvailableArtists

    @PUT("establishment/playlist/artists/initial")
    suspend fun setInitialArtists(@Body manipulateArtistRequest: InitialArtistsRequest): Response<Unit>

    @POST("establishment/devices")
    suspend fun setDevice(@Body setDeviceRequest: DeviceRequest): Response<Unit>

    @POST("establishment/turn-on")
    suspend fun turnOn(): Response<Unit>

    @POST("establishment/turn-off")
    suspend fun turnOff(): Response<Unit>
}