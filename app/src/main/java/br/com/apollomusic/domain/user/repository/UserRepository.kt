package br.com.apollomusic.domain.user.repository

import br.com.apollomusic.data.api.UserApiService
import br.com.apollomusic.domain.user.dto.LoginRequest
import br.com.apollomusic.domain.user.dto.LoginResponse
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: UserApiService
) {

    suspend fun login(username: String, genres: List<String>, establishmentId: Long): LoginResponse {
        try{
            val loginRequest = LoginRequest(username, genres, establishmentId)
            val response = api.login(loginRequest)
            return response
        }catch(e: Exception){
            throw IOException("Falha ao tentar fazer o login.", e)
        }
    }

}