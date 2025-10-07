package br.com.apollomusic.domain.establishment.repository

import br.com.apollomusic.data.api.EstablishmentApiService
import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.domain.establishment.dto.artist.EstablishmentAvailableArtists
import br.com.apollomusic.domain.establishment.dto.EstablishmentForUsersResponse
import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.artist.InitialArtistsRequest
import br.com.apollomusic.domain.establishment.dto.artist.ArtistsResponse
import br.com.apollomusic.domain.establishment.dto.device.DeviceRequest
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

    suspend fun searchForArtists( query: String): List<Artist> {
        try{
            val response = api.searchForArtists(query)
            return response
        }catch(e: Exception){
            throw IOException("Falha ao buscar os artistas", e)
        }
    }

    suspend fun userSearchForArtists(establishmentId: Long, query: String): List<Artist> {
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

    suspend fun setDevice(id: String): Unit {
        try {
            api.setDevice(DeviceRequest(id))
        }catch(e: Exception){
            throw IOException("Falha ao enviar o dispostivo no estabelecimento", e)
        }
    }

    suspend fun setInitialArtists(request: InitialArtistsRequest): Unit {
        try {
            api.setInitialArtists(request)
        }catch(e: Exception){
            throw IOException("Falha ao enviar os artistas", e)
        }
    }

    suspend fun turnOn(): Unit {
        try {
            api.turnOn()
        } catch (e: Exception) {
            throw IOException("Falha ao ligar o estabelecimento", e)
        }
    }

    suspend fun turnOff(): Unit {
        try {
            api.turnOff()
        } catch (e: Exception) {
            throw IOException("Falha ao desligar o estabelecimento", e)
        }
    }

}