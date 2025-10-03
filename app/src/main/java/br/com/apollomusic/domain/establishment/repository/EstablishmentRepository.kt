package br.com.apollomusic.domain.establishment.repository

import br.com.apollomusic.data.api.EstablishmentApiService
import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.device.Device
import br.com.apollomusic.domain.establishment.dto.device.DeviceResponse
import java.io.IOException
import javax.inject.Inject

class EstablishmentRepository @Inject constructor(
    private val api: EstablishmentApiService
) {
    suspend fun getEstablishment(): EstablishmentResponse {
        try{
            val response = api.getEstablishment()
            return response
        }catch(e: Exception){
            throw IOException("Falha ao buscar dados do estabelecimento", e)
        }
    }

    suspend fun getDevice(): DeviceResponse {
        try{
            val response = api.getDevices()
            return response
        }catch(e: Exception){
            throw IOException("Falha ao buscar os dispositivos do estabelecimento", e)
        }
    }
}