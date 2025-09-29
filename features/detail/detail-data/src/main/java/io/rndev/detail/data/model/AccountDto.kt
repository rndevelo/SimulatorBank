package io.rndev.detail.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Accounts
@Serializable
data class AccountResponseDto(
    @SerialName("Data")
    val data: AccountDataDto
)

@Serializable
data class AccountDataDto(
    @SerialName("Account")
    val account: AccountDto
)

@Serializable
data class AccountDto(
    @SerialName("AccountId")
    val accountId: String,
    @SerialName("AccountType")
    val accountType: String,
    @SerialName("AccountSubType")
    val accountSubType: String,
    @SerialName("Currency")
    val currency: String,
    @SerialName("Description")
    val description: String? = null,
    @SerialName("Nickname")
    val nickname: String? = null,
    @SerialName("OpeningDate")
    val openingDate: String,
    @SerialName("Balance")
    val balance: String,
)

