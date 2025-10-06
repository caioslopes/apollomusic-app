package br.com.apollomusic.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT

data class DecodedJwt(
    val username: String,
    val establishmentId: String,
    val artists: List<String>
)

object JwtUtils {
    fun decode(token: String): DecodedJwt? {
        return try {
            val decodedJWT: DecodedJWT = JWT.decode(token)

            val username = decodedJWT.subject
            val establishmentId = decodedJWT.getClaim("establishmentId").asInt()?.toString()
            val artists = decodedJWT.getClaim("genres").asList(String::class.java)

            if (username == null || establishmentId == null || artists == null) {
                return null
            }

            DecodedJwt(
                username = username,
                establishmentId = establishmentId,
                artists = artists
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}