package io.rndev.account_domain.usecases

import io.rndev.account_domain.AccountRepository
import io.rndev.account_domain.model.Account
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): List<Account> = repository.getAccounts()
}

