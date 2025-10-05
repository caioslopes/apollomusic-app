package br.com.apollomusic.domain.establishment.repository

import br.com.apollomusic.data.api.EstablishmentApiService
import br.com.apollomusic.domain.establishment.dto.ArtistsResponse
import br.com.apollomusic.domain.establishment.dto.EstablishmentAvailableArtists
import br.com.apollomusic.domain.establishment.dto.EstablishmentForUsersResponse
import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.device.DeviceResponse
import br.com.apollomusic.domain.establishment.dto.playlist.PlaylistResponse
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

    suspend fun getEstablishmentById(id: String): EstablishmentForUsersResponse {
        try{
            val response = api.getEstablishment(id)
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

    suspend fun getPlaylist(): PlaylistResponse {
        try{
            val response = api.getPlaylist()
            return response
        }catch(e: Exception){
            throw IOException("Falha ao buscar a playlist do estabelecimento", e)
        }
    }

    suspend fun createPlaylist(): Unit {
        try{
            api.createPlaylist()
        }catch(e: Exception){
            throw IOException("Falha ao criar a playlist do estabelecimento", e)
        }
    }

    suspend fun userSearchForArtists(establishmentId: Long, query: String): ArtistsResponse {
        try{
            val response = api.userSearchForArtists(establishmentId, query)
            return response
        }catch(e: Exception){
            throw IOException("Falha ao buscar os artistas", e)
        }
    }

    suspend fun getEstablishmentAvailableArtists(id: String): EstablishmentAvailableArtists {
        try{
            val response = api.getEstablishmentAvailableArtists(id)
            return response
        }catch(e: Exception){
            throw IOException("Falha ao buscar os artistas disponíveis no estabelecimento", e)
        }
    }
}