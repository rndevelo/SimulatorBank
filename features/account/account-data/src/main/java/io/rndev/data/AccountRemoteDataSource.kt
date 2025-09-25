package io.rndev.data

import io.rndev.data.model.AccountDto
import io.rndev.data.model.BalanceDto
import io.rndev.data.model.PartyDto
import io.rndev.data.model.SandboxRequestDto
import io.rndev.data.model.SandboxResponseDto
import io.rndev.data.model.TransactionDto
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(private val api: AccountApiService) :
    AccountRemoteDataSource {

    override suspend fun getAccounts() = api.getAccounts().data.account
    override suspend fun getAccount(accountId: String) = api.getAccount(accountId)
    override suspend fun getTransactions(accountId: String) =
        api.getTransactions(accountId).data.transaction

    override suspend fun getBalances(accountId: String) = api.getBalances(accountId).data.balance
    override suspend fun sandboxOperation(request: SandboxRequestDto) =
        api.sandboxOperation(request)

    override suspend fun getParty() = api.getParty().data.party
}

interface AccountRemoteDataSource {
    suspend fun getAccounts(): List<AccountDto>
    suspend fun getAccount(accountId: String): AccountDto
    suspend fun getTransactions(accountId: String): List<TransactionDto>
    suspend fun getBalances(accountId: String): List<BalanceDto>
    suspend fun sandboxOperation(request: SandboxRequestDto): SandboxResponseDto
    suspend fun getParty(): PartyDto
}

