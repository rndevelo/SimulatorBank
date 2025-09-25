package io.rndev.domain.usecases

import io.rndev.domain.AccountRepository
import io.rndev.domain.model.Transaction
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(accountId: String): List<Transaction> =
        repository.getTransactions(accountId)
}
