package io.rndev.detail.data

import io.rndev.detail.data.model.AccountDto
import io.rndev.detail.data.model.AccountResponseDto
import io.rndev.detail.data.model.BalanceDto
import io.rndev.detail.data.model.PartyDto
import io.rndev.detail.data.model.SandboxRequestDto
import io.rndev.detail.data.model.SandboxResponseDto
import io.rndev.detail.data.model.TransactionDto
import javax.inject.Inject

class DetailRemoteDataSourceImpl @Inject constructor(private val api: DetailApiService) :
    DetailRemoteDataSource {

    override suspend fun getAccount(accountId: String) = api.getAccount(accountId)
    override suspend fun getTransactions(accountId: String) =
        api.getTransactions(accountId).data.transactions

    override suspend fun getBalances(accountId: String) = api.getBalances(accountId).data.balances
    override suspend fun sandboxOperation(request: SandboxRequestDto) =
        api.sandboxOperation(request)

    override suspend fun getParty() = api.getParty().data.party
}

interface DetailRemoteDataSource {
    suspend fun getAccount(accountId: String): AccountResponseDto
    suspend fun getTransactions(accountId: String): List<TransactionDto>
    suspend fun getBalances(accountId: String): List<BalanceDto>
    suspend fun sandboxOperation(request: SandboxRequestDto): SandboxResponseDto
    suspend fun getParty(): PartyDto
}

