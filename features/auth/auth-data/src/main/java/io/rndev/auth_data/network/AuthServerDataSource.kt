package io.rndev.auth_data.network

import android.util.Log
import javax.inject.Inject

internal class AuthServerDataSource @Inject constructor(private val api: AuthApi) :
    AuthRemoteDataSource {

    override suspend fun login(
        username: String,
        password: String,
    ): Result<LoginResponse> {
        try {
            val response = api.login(LoginRequest(username, password))
            val result = Result.success(response)

            Log.d("UserAuthResult", "result: $result")

//            _uiState.value = _uiState.value.copy(user = result)
//                if (result.isSuccess) {
//                    onSuccess()
//                } else {
//                    _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message)
//                }
        } catch (e: Exception) {
//            val result = Result.failure(e)
        }
    }
}

interface AuthRemoteDataSource {
    suspend fun login(username: String, password: String): Result<LoginResponse>
}
