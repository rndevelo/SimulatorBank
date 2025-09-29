package io.rndev.detail.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsResponseDto(
    @SerialName("Data")
    val data: TransactionsDataDto
)

@Serializable
data class TransactionsDataDto(
    @SerialName("Transaction")
    val transactions: List<TransactionDto>
)

@Serializable
data class TransactionDto(
    @SerialName("TransactionId")
    val transactionId: String,
    @SerialName("AccountId")
    val accountId: String,
    @SerialName("Amount")
    val amount: AmountDto,
    @SerialName("BookingDateTime")
    val bookingDateTime: String,
    @SerialName("CreditDebitIndicator")
    val creditDebitIndicator: String,
    @SerialName("Status")
    val status: String,
    @SerialName("TransactionReference")
    val transactionReference: String,
    @SerialName("TransactionInformation")
    val transactionInformation: String
)

@Serializable
data class AmountDto(
    @SerialName("Amount")
    val amount: String,
    @SerialName("Currency")
    val currency: String
)
