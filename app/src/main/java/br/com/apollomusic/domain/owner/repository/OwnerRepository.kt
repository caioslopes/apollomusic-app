package br.com.apollomusic.domain.owner.repository

import br.com.apollomusic.data.api.OwnerApiService
import br.com.apollomusic.domain.owner.dto.LoginRequest
import br.com.apollomusic.domain.owner.dto.LoginResponse
import br.com.apollomusic.domain.owner.dto.OwnerResponse
import br.com.apollomusic.network.NetworkResult
import br.com.apollomusic.network.safeApiCall
import retrofit2.HttpException
import javax.inject.Inject

class OwnerRepository @Inject constructor(
    private val api: OwnerApiService
) {

    suspend fun login(email: String, password: String, establishmentId: String): NetworkResult<LoginResponse> {
        return safeApiCall(
            customMessages = mapOf(401 to "Credênciais inválidas.")
        ) {
            val loginRequest = LoginRequest(email, password, establishmentId)
            api.login(loginRequest)
        }
    }

    suspend fun sendSpotifyAuthorizationCode(code: String): NetworkResult<Unit> {
        return safeApiCall {
            val response = api.sendSpotifyCode(mapOf("code" to code))
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }
    }

    suspend fun getOwner(): NetworkResult<OwnerResponse> {
        return safeApiCall {
            api.getOwner()
        }
    }
}