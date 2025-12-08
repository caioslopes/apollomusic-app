package br.com.apollomusic.data.api

import br.com.apollomusic.domain.location.dto.VerifyLocationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationApiService {
    @POST("location")
    suspend fun verifyLocationEstablishment(
        @Body request: VerifyLocationRequest
    ): Response<Unit>
}