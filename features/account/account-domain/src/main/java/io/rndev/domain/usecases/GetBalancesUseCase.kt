package io.rndev.domain.usecases

import io.rndev.domain.AccountRepository
import io.rndev.domain.model.Balance
import javax.inject.Inject

class GetBalancesUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(accountId: String): List<Balance> = repository.getBalances(accountId)
}
