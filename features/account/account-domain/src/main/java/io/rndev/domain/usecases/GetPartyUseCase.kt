package io.rndev.domain.usecases

import io.rndev.domain.AccountRepository
import io.rndev.domain.model.Party
import javax.inject.Inject

class GetPartyUseCase @Inject constructor(private val repository: AccountRepository) {
    suspend operator fun invoke(): Party = repository.getParty()
}
