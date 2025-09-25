package io.rndev.data // O tu paquete

import io.rndev.domain.AuthException // Importa tu AuthException
import java.io.IOException
import javax.inject.Inject // Si usas Hilt/Dagger

class AuthServerDataSource @Inject constructor(
    private val authApiService: AuthApiService
): AuthRemoteDataSource {

    override suspend fun login(username: String, password: String): Result<AuthDto> {
        return try {
            val response = authApiService.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val authError = mapError(response.code(), response.message())
                Result.failure(authError)
            }
        } catch (e: IOException) {
            Result.failure(AuthException.NetworkError(e))
        }
    }
    // Implementa otras funciones como register de forma similar

    private companion object {
        private const val UNAUTHORIZED = 401
        private const val NOT_FOUND = 404
        private const val INTERNAL_SERVER_ERROR = 500
        private const val SERVICE_UNAVAILABLE = 503
    }

    private fun mapError(code: Int, message: String?): AuthException {
        return when (code) {
            UNAUTHORIZED -> AuthException.InvalidCredentials
            NOT_FOUND -> AuthException.UserNotFound
            INTERNAL_SERVER_ERROR, SERVICE_UNAVAILABLE -> AuthException.ServerError(code, message)
            else -> AuthException.UnknownError("Error HTTP $code: ${message ?: "Sin mensaje"}", null)
        }
    }
}

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): Result<AuthDto>
}
