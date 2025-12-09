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
import br.com.apollomusic.domain.establishment.dto.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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
    suspend fun createPlaylist()

    @GET("establishment/playlist/search-artists")
    suspend fun searchForArtists(@Query("query") query: String): List<Artist>

    @GET("establishment/playlist/for-user/search-artists")
    suspend fun userSearchForArtists(@Query("establishmentId") establishmentId: Long, @Query("query") query: String): List<Artist>

    @GET("establishment/playlist/genres/{id}")
    suspend fun getEstablishmentAvailableArtists(@Path("id") id: String): EstablishmentAvailableArtists

    @PUT("establishment/playlist/artists/initial")
    suspend fun setInitialArtists(@Body manipulateArtistRequest: InitialArtistsRequest)

    @POST("establishment/devices")
    suspend fun setDevice(@Body setDeviceRequest: DeviceRequest)

    @Headers("Accept: */*")
    @POST("establishment/turn-on")
    suspend fun turnOn()

    @Headers("Accept: */*")
    @POST("establishment/turn-off")
    suspend fun turnOff()

    @GET("establishment/posts")
    suspend fun getPosts(@Query("establishmentId") establishmentId: Long): List<Post>

    @Multipart
    @POST("establishment/posts")
    suspend fun createPost(
        @Part("username") username: RequestBody,
        @Part("content") content: RequestBody,
        @Part("establishmentId") establishmentId: RequestBody,
        @Part images: List<MultipartBody.Part>
    ): Post
}