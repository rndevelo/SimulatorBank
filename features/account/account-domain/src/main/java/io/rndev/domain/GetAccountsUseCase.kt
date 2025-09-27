package io.rndev.domain

import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(private val repository: AccountsRepository) {
    suspend operator fun invoke(): Result<List<Account>> = repository.getAccounts()
}
