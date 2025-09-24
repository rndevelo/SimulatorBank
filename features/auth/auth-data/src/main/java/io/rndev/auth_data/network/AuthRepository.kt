package io.rndev.auth_data.network

import io.rndev.auth_data.network.token.TokenProvider
import io.rndev.auth_domain.AuthRepository
import io.rndev.auth_domain.User
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDt: AuthRemoteDataSource,
    private val tokenProvider: TokenProvider
) : AuthRepository {
    override suspend fun login(username: String, password: String): User {
        val response = authDt.login(username, password)
        tokenProvider.saveToken(response.access_token)
        return response.toDomainUser()
    }
}

private fun LoginResponse.toDomainUser() = User(
    id = user.id,
    username = user.username,
    name = user.name
)
