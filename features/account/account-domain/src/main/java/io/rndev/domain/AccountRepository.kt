package io.rndev.domain

import io.rndev.domain.model.Account
import io.rndev.domain.model.Balance
import io.rndev.domain.model.Party
import io.rndev.domain.model.Transaction

interface AccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun getAccount(accountId: String): Account
    suspend fun getTransactions(accountId: String): List<Transaction>
    suspend fun getBalances(accountId: String): List<Balance>
    suspend fun getParty(): Party
//    suspend fun sandboxOperation(request: SandboxRequest): SandboxResult
}
