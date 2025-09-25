package io.rndev.domain

interface AccountRepository {
    suspend fun getAccounts(): List<Account>
}
