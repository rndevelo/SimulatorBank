package io.rndev.detail.domain

import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Party
import io.rndev.detail.domain.model.Transaction

interface DetailRepository {
    suspend fun getAccount(accountId: String): Result<Account>
    suspend fun getBalances(accountId: String): Result<List<Balance>>
    suspend fun getTransactions(accountId: String): Result<List<Transaction>>

    suspend fun getParty(): Result<Party>
//    suspend fun sandboxOperation(request: SandboxRequest): SandboxResult
}
