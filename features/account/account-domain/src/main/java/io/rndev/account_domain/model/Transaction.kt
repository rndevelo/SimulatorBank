package io.rndev.account_domain.model

// Transaction
data class Transaction(
    val accountId: String,
    val amount: String,
    val currency: String,
    val bookingDateTime: String,
    val creditDebitIndicator: String,
    val description: String?,
    val reference: String?
)
