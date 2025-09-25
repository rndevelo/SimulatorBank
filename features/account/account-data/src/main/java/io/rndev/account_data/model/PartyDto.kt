package io.rndev.account_data.model

import kotlinx.serialization.Serializable

// Party
@Serializable
data class PartyDto(
    val partyId: String,
    val name: String
)

@Serializable
data class PartyResponseDto(
    val data: PartyDataDto
)

@Serializable
data class PartyDataDto(
    val party: PartyDto
)
