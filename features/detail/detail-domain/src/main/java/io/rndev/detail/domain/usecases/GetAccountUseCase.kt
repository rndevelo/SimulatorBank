package io.rndev.detail.domain.usecases

import io.rndev.detail.domain.DetailRepository
import io.rndev.detail.domain.model.Account
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(private val repository: DetailRepository) {
    suspend operator fun invoke(accountId: String): Result<Account> = repository.getAccount(accountId)
}
