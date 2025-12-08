package br.com.apollomusic.domain.user.repository

import br.com.apollomusic.data.api.UserApiService
import br.com.apollomusic.domain.user.dto.LoginRequest
import br.com.apollomusic.domain.user.dto.LoginResponse
import br.com.apollomusic.network.NetworkResult
import br.com.apollomusic.network.safeApiCall
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApiService
) {

    suspend fun login(username: String, genres: List<String>, establishmentId: Long): NetworkResult<LoginResponse> {
        return safeApiCall {
            val loginRequest = LoginRequest(username, genres, establishmentId)
            api.login(loginRequest)
        }
    }
}
