package io.rndev.data

import io.rndev.data.model.AccountDto
import io.rndev.domain.AccountsException
import java.io.IOException
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(private val api: AccountsApiService) :
    AccountRemoteDataSource {

    override suspend fun getAccounts(): Result<List<AccountDto>> {
        return try {
            val response = api.getAccounts()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data.accounts)
            } else {
                val authError = mapError(response.code(), response.message())
                Result.failure(authError)
            }
        } catch (e: IOException) {
            Result.failure(AccountsException.NetworkError(e))
        }
    }

    private companion object {
        private const val UNAUTHORIZED = 401
        private const val NOT_FOUND = 404
        private const val INTERNAL_SERVER_ERROR = 500
        private const val SERVICE_UNAVAILABLE = 503
    }

    private fun mapError(code: Int, message: String?): Exception {
        return when (code) {
            UNAUTHORIZED -> AccountsException.InvalidCredentials
            NOT_FOUND -> AccountsException.UserNotFound
            INTERNAL_SERVER_ERROR, SERVICE_UNAVAILABLE -> AccountsException.ServerError(code, message)
            else -> AccountsException.UnknownError("Error HTTP $code: ${message ?: "Sin mensaje"}", null)
        }
    }
}

interface AccountRemoteDataSource {
    suspend fun getAccounts(): Result<List<AccountDto>>
}
