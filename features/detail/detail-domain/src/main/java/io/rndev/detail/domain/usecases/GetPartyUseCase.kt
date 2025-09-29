package io.rndev.detail.domain.usecases

import io.rndev.detail.domain.DetailRepository
import io.rndev.detail.domain.model.Party
import javax.inject.Inject

class GetPartyUseCase @Inject constructor(private val repository: DetailRepository) {
    suspend operator fun invoke(): Result<Party> = repository.getParty()
}
