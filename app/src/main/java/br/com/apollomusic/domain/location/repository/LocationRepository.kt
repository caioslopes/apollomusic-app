package br.com.apollomusic.domain.location.repository

import br.com.apollomusic.data.api.LocationApiService
import br.com.apollomusic.domain.location.dto.VerifyLocationRequest
import br.com.apollomusic.network.NetworkResult
import br.com.apollomusic.network.safeApiCall
import retrofit2.HttpException
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val api: LocationApiService
) {

    suspend fun sendLocation(request: VerifyLocationRequest): NetworkResult<Unit> {
        return safeApiCall {
            val response = api.verifyLocationEstablishment(request)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }
    }
}
