package io.rndev.detail.domain.model

// Balance
data class Balance(
    val accountId: String,
    val amount: Double,
    val currency: String,
    val creditDebitIndicator: String,
    val type: String
)
