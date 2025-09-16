package br.com.apollomusic.data.api

import br.com.apollomusic.domain.owner.dto.LoginRequest
import br.com.apollomusic.domain.owner.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OwnerApiService {

    @POST("auth/owner")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}