package io.rndev.auth_data

import io.rndev.auth_domain.AuthException
import io.rndev.auth_domain.AuthRepository
import io.rndev.auth_domain.User
import io.rndev.core.common.TokenProvider
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDt: AuthRemoteDataSource,
    private val tokenProvider: TokenProvider
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        // Mapea credenciales de dominio a DTO de red si es necesario
        val requestDto = authDt.login(username, password)

        return requestDto.mapCatching { authDto ->
            tokenProvider.saveToken(authDto.access_token)
            // .mapCatching para seguridad adicional, aunque el Result ya maneja excepciones
            // Aquí conviertes tu UserTokenDto a tu UserModel del dominio
            authDto.toDomainModel() // Usando una función de extensión como ejemplo
        }.recoverCatching { throwable ->
            // Si el mapeo a DomainModel falla (aunque es raro si el DTO es correcto),
            // o si quieres convertir cualquier Throwable no-AuthException a uno.
            if (throwable is AuthException) throw throwable
            else throw AuthException.UnknownError("Error al procesar la respuesta del servidor.", throwable)
        }
    }
}

private fun AuthDto.toDomainModel() = User(
    id = user.id,
    username = user.username,
    name = user.name
)
