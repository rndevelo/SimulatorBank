package io.rndev.account_domain.model

// Balance
data class Balance(
    val accountId: String,
    val amount: String,
    val currency: String,
    val creditDebitIndicator: String
)
