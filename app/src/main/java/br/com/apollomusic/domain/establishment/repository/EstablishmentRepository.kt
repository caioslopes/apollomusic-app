package br.com.apollomusic.domain.establishment.repository

import android.content.Context
import android.net.Uri
import br.com.apollomusic.data.api.EstablishmentApiService
import br.com.apollomusic.domain.establishment.dto.artist.Artist
import br.com.apollomusic.domain.establishment.dto.artist.EstablishmentAvailableArtists
import br.com.apollomusic.domain.establishment.dto.EstablishmentForUsersResponse
import br.com.apollomusic.domain.establishment.dto.EstablishmentResponse
import br.com.apollomusic.domain.establishment.dto.Post
import br.com.apollomusic.domain.establishment.dto.artist.InitialArtistsRequest
import br.com.apollomusic.domain.establishment.dto.device.DeviceRequest
import br.com.apollomusic.domain.establishment.dto.device.DeviceResponse
import br.com.apollomusic.domain.establishment.dto.playlist.PlaylistResponse
import br.com.apollomusic.network.NetworkResult
import br.com.apollomusic.network.safeApiCall
import br.com.apollomusic.utils.uriToMultipartBodyPart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class EstablishmentRepository @Inject constructor(
    private val api: EstablishmentApiService
) {
    suspend fun getEstablishment(): NetworkResult<EstablishmentResponse> {
        return safeApiCall { api.getEstablishment() }
    }

    suspend fun getEstablishmentById(id: String): NetworkResult<EstablishmentForUsersResponse> {
        return safeApiCall { api.getEstablishment(id) }
    }

    suspend fun getDevice(): NetworkResult<DeviceResponse> {
        return safeApiCall { api.getDevices() }
    }

    suspend fun getPlaylist(): NetworkResult<PlaylistResponse> {
        return safeApiCall { api.getPlaylist() }
    }

    suspend fun createPlaylist(): NetworkResult<Unit> {
        return safeApiCall { api.createPlaylist() }
    }

    suspend fun searchForArtists(query: String): NetworkResult<List<Artist>> {
        return safeApiCall { api.searchForArtists(query) }
    }

    suspend fun userSearchForArtists(establishmentId: Long, query: String): NetworkResult<List<Artist>> {
        return safeApiCall { api.userSearchForArtists(establishmentId, query) }
    }

    suspend fun getEstablishmentAvailableArtists(id: String): NetworkResult<EstablishmentAvailableArtists> {
        return safeApiCall { api.getEstablishmentAvailableArtists(id) }
    }

    suspend fun setDevice(id: String): NetworkResult<Unit> {
        return safeApiCall { api.setDevice(DeviceRequest(id)) }
    }

    suspend fun setInitialArtists(request: InitialArtistsRequest): NetworkResult<Unit> {
        return safeApiCall { api.setInitialArtists(request) }
    }

    suspend fun turnOn(): NetworkResult<Unit> {
        return safeApiCall { api.turnOn() }
    }

    suspend fun turnOff(): NetworkResult<Unit> {
        return safeApiCall { api.turnOff() }
    }

    suspend fun getPosts(establishmentId: Long): NetworkResult<List<Post>> {
        return safeApiCall { api.getPosts(establishmentId) }
    }

    suspend fun createPost(
        context: Context,
        username: String,
        content: String,
        establishmentId: Long,
        imageUris: List<Uri>
    ): NetworkResult<Post> {
        return safeApiCall {
            val usernamePart = username.toRequestBody("text/plain".toMediaTypeOrNull())
            val contentPart = content.toRequestBody("text/plain".toMediaTypeOrNull())
            val establishmentIdPart = establishmentId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val imageParts = imageUris.mapNotNull { uri ->
                uriToMultipartBodyPart(context, uri, "images")
            }

            if (imageParts.isEmpty()) {
                throw IllegalArgumentException("Pelo menos uma imagem é necessária.")
            }

            api.createPost(usernamePart, contentPart, establishmentIdPart, imageParts)
        }
    }
}