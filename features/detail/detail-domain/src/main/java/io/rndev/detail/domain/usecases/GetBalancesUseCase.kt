package io.rndev.detail.domain.usecases

import io.rndev.detail.domain.DetailRepository
import io.rndev.detail.domain.model.Balance
import javax.inject.Inject

class GetBalancesUseCase @Inject constructor(private val repository: DetailRepository) {
    suspend operator fun invoke(accountId: String): List<Balance> = repository.getBalances(accountId)
}
