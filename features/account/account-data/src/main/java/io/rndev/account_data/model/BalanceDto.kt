package io.rndev.account_data.model

import kotlinx.serialization.Serializable

// Balances
@Serializable
data class BalanceDto(
    val accountId: String,
    val amount: String,
    val currency: String,
    val creditDebitIndicator: String
)

@Serializable
data class BalancesResponseDto(
    val data: BalancesDataDto
)

@Serializable
data class BalancesDataDto(
    val balance: List<BalanceDto>
)
