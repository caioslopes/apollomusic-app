package br.com.apollomusic.data.api

import br.com.apollomusic.domain.user.dto.LoginRequest
import br.com.apollomusic.domain.user.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {

    @POST("auth/user")
    suspend fun login(@Body request: LoginRequest): LoginResponse

}