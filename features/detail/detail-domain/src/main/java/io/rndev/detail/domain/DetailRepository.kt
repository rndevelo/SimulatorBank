package io.rndev.detail.domain

import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Party
import io.rndev.detail.domain.model.Transaction

interface DetailRepository {
    suspend fun getAccount(accountId: String): Account
    suspend fun getTransactions(accountId: String): List<Transaction>
    suspend fun getBalances(accountId: String): List<Balance>
    suspend fun getParty(): Party
//    suspend fun sandboxOperation(request: SandboxRequest): SandboxResult
}
