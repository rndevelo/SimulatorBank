package io.rndev.account_domain.usecases

import io.rndev.account_domain.AccountRepository
import io.rndev.account_domain.model.Balance
import javax.inject.Inject

class GetBalancesUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(accountId: String): List<Balance> = repository.getBalances(accountId)
}
