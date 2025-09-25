package io.rndev.account_domain

import io.rndev.account_domain.model.Account
import io.rndev.account_domain.model.Balance
import io.rndev.account_domain.model.Party
import io.rndev.account_domain.model.SandboxRequest
import io.rndev.account_domain.model.Transaction

interface AccountRepository {
    suspend fun getAccounts(): List<Account>
    suspend fun getAccount(accountId: String): Account
    suspend fun getTransactions(accountId: String): List<Transaction>
    suspend fun getBalances(accountId: String): List<Balance>
    suspend fun getParty(): Party
//    suspend fun sandboxOperation(request: SandboxRequest): SandboxResult
}
