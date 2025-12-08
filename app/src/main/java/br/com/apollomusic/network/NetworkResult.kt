package br.com.apollomusic.network

import retrofit2.HttpException
import java.io.IOException

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}

suspend fun <T> safeApiCall(
    customMessages: Map<Int, String> = emptyMap(),
    apiCall: suspend () -> T
): NetworkResult<T> {
    return try {
        val result = apiCall()
        NetworkResult.Success(result)
    } catch (throwable: Throwable) {
        when (throwable) {
            is IOException -> NetworkResult.Error("Erro de conexão. Verifique sua internet.")
            is HttpException -> {
                val code = throwable.code()
                val message = customMessages[code] ?: when (code) {
                    401 -> "Sessão expirada. Faça login novamente."
                    403 -> "Acesso negado."
                    404 -> "Recurso não encontrado."
                    in 500..599 -> "Erro interno no servidor."
                    else -> "Erro na requisição ($code)."
                }
                NetworkResult.Error(message)
            }
            else -> NetworkResult.Error("Erro inesperado: ${throwable.localizedMessage}")
        }
    }
}