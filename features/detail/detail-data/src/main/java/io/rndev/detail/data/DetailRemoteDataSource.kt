package io.rndev.detail.data

// Asumimos que estos DTOs wrapper existen o se crearán
import io.rndev.detail.data.model.AccountResponseDto
import io.rndev.detail.data.model.BalanceDto
import io.rndev.detail.data.model.PartyDto
import io.rndev.detail.data.model.SandboxRequestDto
import io.rndev.detail.data.model.SandboxResponseDto
import io.rndev.detail.data.model.TransactionDto
import io.rndev.detail.domain.DetailException
import java.io.IOException
import javax.inject.Inject

interface DetailRemoteDataSource {
    suspend fun getAccount(accountId: String): Result<AccountResponseDto> // Devuelve el wrapper DTO completo
    suspend fun getTransactions(accountId: String): Result<List<TransactionDto>>
    suspend fun getBalances(accountId: String): Result<List<BalanceDto>>
    suspend fun sandboxOperation(request: SandboxRequestDto): Result<SandboxResponseDto> // Asumiendo que esta también debe devolver Result
    suspend fun getParty(): Result<PartyDto>
}

class DetailRemoteDataSourceImpl @Inject constructor(
    private val api: DetailApiService
) : DetailRemoteDataSource {

    override suspend fun getAccount(accountId: String): Result<AccountResponseDto> {
        return try {
            val response = api.getAccount(accountId) // Asume que api.getAccount devuelve Response<AccountResponseDto>
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(mapError(response.code(), response.message()))
            }
        } catch (e: IOException) {
            Result.failure(DetailException.NetworkError(e))
        }
    }

    override suspend fun getTransactions(accountId: String): Result<List<TransactionDto>> {
        return try {
            // Asume que api.getTransactions devuelve Response<TransactionsResponseDto>
            val response = api.getTransactions(accountId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data.transactions)
            } else {
                Result.failure(mapError(response.code(), response.message()))
            }
        } catch (e: IOException) {
            Result.failure(DetailException.NetworkError(e))
        }
    }

    override suspend fun getBalances(accountId: String): Result<List<BalanceDto>> {
        return try {
            // Asume que api.getBalances devuelve Response<BalancesResponseDto>
            // y BalancesResponseDto tiene una estructura como .data.balances
            val response = api.getBalances(accountId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data.balances)
            } else {
                Result.failure(mapError(response.code(), response.message()))
            }
        } catch (e: IOException) {
            Result.failure(DetailException.NetworkError(e))
        }
    }

    override suspend fun sandboxOperation(request: SandboxRequestDto): Result<SandboxResponseDto> {
        return try {
            val response = api.sandboxOperation(request) // Asume que api.sandboxOperation devuelve Response<SandboxResponseDto>
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(mapError(response.code(), response.message()))
            }
        } catch (e: IOException) {
            Result.failure(DetailException.NetworkError(e))
        }
    }

    override suspend fun getParty(): Result<PartyDto> {
        return try {
            // Asume que api.getParty devuelve Response<PartyResponseDto>
            // y PartyResponseDto tiene una estructura como .data.party
            val response = api.getParty()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data.party)
            } else {
                Result.failure(mapError(response.code(), response.message()))
            }
        } catch (e: IOException) {
            Result.failure(DetailException.NetworkError(e))
        }
    }

    // Helper function para mapear errores HTTP, similar a AccountRemoteDataSourceImpl
    private fun mapError(code: Int, message: String?): Exception {
        return when (code) {
            UNAUTHORIZED -> DetailException.InvalidCredentials
            NOT_FOUND -> DetailException.UserNotFound
            INTERNAL_SERVER_ERROR, SERVICE_UNAVAILABLE -> DetailException.ServerError(code, message)
            else -> DetailException.UnknownError("Error HTTP $code: ${message ?: "Sin mensaje"}", null)
        }
    }

    private companion object {
        private const val UNAUTHORIZED = 401
        private const val NOT_FOUND = 404
        private const val INTERNAL_SERVER_ERROR = 500
        private const val SERVICE_UNAVAILABLE = 503
    }
}
