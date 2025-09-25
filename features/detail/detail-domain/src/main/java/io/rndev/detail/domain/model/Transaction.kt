package io.rndev.detail.domain.model

// Transaction
data class Transaction(
    val accountId: String,
    val amount: Double,
    val currency: String,
    val bookingDateTime: String,
    val creditDebitIndicator: String,
    val description: String?,
    val reference: String
)
