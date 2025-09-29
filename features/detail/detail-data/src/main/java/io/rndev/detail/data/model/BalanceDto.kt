package io.rndev.detail.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BalancesResponseDto(
    @SerialName("Data")
    val data: BalancesDataDto
)

@Serializable
data class BalancesDataDto(
    @SerialName("Balance")
    val balances: List<BalanceDto>
)

@Serializable
data class BalanceDto(
    @SerialName("AccountId")
    val accountId: String,
    @SerialName("Amount")
    val amount: AmountDto,
    @SerialName("CreditDebitIndicator")
    val creditDebitIndicator: String,
    @SerialName("Type")
    val type: String,
    @SerialName("DateTime")
    val dateTime: String
)
