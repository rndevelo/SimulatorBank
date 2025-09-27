package io.rndev.domain

interface AccountsRepository {
    suspend fun getAccounts(): Result<List<Account>>
}
