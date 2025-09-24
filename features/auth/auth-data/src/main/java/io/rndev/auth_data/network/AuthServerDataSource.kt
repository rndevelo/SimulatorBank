package io.rndev.auth_data.network

import android.util.Log
import javax.inject.Inject

internal class AuthServerDataSource @Inject constructor(private val api: AuthApi) :
    AuthRemoteDataSource {

    override suspend fun login(
        username: String,
        password: String,
    ) = api.login(LoginRequest(username, password))
}

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): LoginResponse
}
