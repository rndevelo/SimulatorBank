package io.rndev.data

import android.util.Log
import io.rndev.core.common.TokenProvider
import io.rndev.domain.AuthRepository
import io.rndev.domain.User
import javax.inject.Inject
import io.rndev.domain.AuthException

class AuthRepositoryImpl @Inject constructor(
    private val authDt: AuthRemoteDataSource,
    private val tokenProvider: TokenProvider
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        // Mapea credenciales de dominio a DTO de red si es necesario
        val requestDto = authDt.login(username, password)

        return requestDto.mapCatching { authDto ->
            tokenProvider.saveToken(authDto.accessToken)
            // .mapCatching para seguridad adicional, aunque el Result ya maneja excepciones
            // Aquí conviertes tu UserTokenDto a tu UserModel del dominio
            authDto.user?.toDomainModel()!! // Usando una función de extensión como ejemplo
        }.recoverCatching { throwable ->
            Log.d("AuthRepositoryImpl", "throwable: $throwable")
            // Si el mapeo a DomainModel falla (aunque es raro si el DTO es correcto),
            // o si quieres convertir cualquier Throwable no-AuthException a uno.
            if (throwable is Exception) throw throwable
            else throw AuthException.ReadServerResponseError
        }
    }
}

private fun UserDto.toDomainModel() = User(
    id = id,
    username = username,
    name = name
)
