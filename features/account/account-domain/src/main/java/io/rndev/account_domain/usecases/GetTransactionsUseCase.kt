package io.rndev.account_domain.usecases

import io.rndev.account_domain.AccountRepository
import io.rndev.account_domain.model.Transaction
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(accountId: String): List<Transaction> =
        repository.getTransactions(accountId)
}
