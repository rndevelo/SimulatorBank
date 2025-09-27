package io.rndev.detail.domain.usecases

import io.rndev.detail.domain.DetailRepository
import io.rndev.detail.domain.model.Transaction
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val repository: DetailRepository) {
    suspend operator fun invoke(accountId: String): Result<List<Transaction>> =
        repository.getTransactions(accountId)
}
