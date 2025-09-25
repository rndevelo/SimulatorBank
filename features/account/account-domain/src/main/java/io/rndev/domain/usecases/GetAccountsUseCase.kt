package io.rndev.domain.usecases

import io.rndev.domain.AccountRepository
import io.rndev.domain.model.Account
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): List<Account> = repository.getAccounts()
}

