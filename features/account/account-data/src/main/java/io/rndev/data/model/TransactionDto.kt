package io.rndev.data.model

import kotlinx.serialization.Serializable

// Transactions
@Serializable
data class TransactionDto(
    val accountId: String,
    val amount: String,
    val currency: String,
    val bookingDateTime: String,
    val creditDebitIndicator: String,
    val description: String?,
    val transactionReference: String?
)

@Serializable
data class TransactionsResponseDto(
    val data: TransactionsDataDto
)

@Serializable
data class TransactionsDataDto(
    val transaction: List<TransactionDto>
)
