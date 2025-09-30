package br.com.apollomusic.data.api

import br.com.apollomusic.domain.owner.dto.LoginRequest
import br.com.apollomusic.domain.owner.dto.LoginResponse
import br.com.apollomusic.domain.owner.dto.OwnerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OwnerApiService {

    @POST("auth/owner")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/api")
    suspend fun sendSpotifyCode(@Body body: Map<String, String>): Response<Unit>

    @GET("owner")
    suspend fun getOwner(): OwnerResponse


}