package io.rndev.auth_data // O tu paquete

import io.rndev.auth_domain.AuthException // Importa tu AuthException
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
        } catch (e: Exception) { // Captura genérica para otros errores inesperados
            Result.failure(AuthException.UnknownError(e.message ?: "Error desconocido durante la llamada de red", e))
        }
    }

    private fun mapError(code: Int, message: String?): AuthException {
        return when (code) {
            401 -> AuthException.InvalidCredentials
            404 -> AuthException.UserNotFound
            // Añade más mapeos de códigos HTTP a tus AuthException específicas
            500, 503 -> AuthException.ServerError(code, message)
            else -> AuthException.UnknownError("Error HTTP $code: ${message ?: "Sin mensaje"}", null)
        }
    }

    // Implementa otras funciones como register de forma similar
}

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): Result<AuthDto>
}
