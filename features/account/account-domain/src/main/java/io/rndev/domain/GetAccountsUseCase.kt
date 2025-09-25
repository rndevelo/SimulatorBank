package io.rndev.domain

import io.rndev.domain.Account
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): List<Account> = repository.getAccounts()
}
