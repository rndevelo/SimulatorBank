package io.rndev.account_domain.usecases

import io.rndev.account_domain.AccountRepository
import io.rndev.account_domain.model.Party
import javax.inject.Inject

class GetPartyUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): Party = repository.getParty()
}
