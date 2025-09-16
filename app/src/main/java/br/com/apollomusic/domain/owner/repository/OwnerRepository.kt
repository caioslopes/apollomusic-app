package br.com.apollomusic.domain.owner.repository

import br.com.apollomusic.data.api.OwnerApiService
import br.com.apollomusic.domain.owner.dto.LoginRequest
import br.com.apollomusic.domain.owner.dto.LoginResponse
import java.io.IOException
import javax.inject.Inject

class OwnerRepository @Inject constructor(
    private val api: OwnerApiService
) {

    suspend fun login(email: String, password: String, establishmentId: String): LoginResponse {
        try{
            val loginRequest = LoginRequest(email, password, establishmentId)
            val response = api.login(loginRequest)
            return response
        }catch(e: Exception){
            throw IOException("Failed to fetch user", e)
        }
    }

}