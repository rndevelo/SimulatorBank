package io.rndev.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Accounts
@Serializable
data class AccountsResponseDto(
    @SerialName("Data")
    val data: AccountsDataDto
)

@Serializable
data class AccountsDataDto(
    @SerialName("Account")
    val account: List<AccountDto>
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
    val balance: Double
)


@Serializable
data class AccountInfoDto(
    @SerialName("Identification")
    val identification: String,
    @SerialName("Name")
    val name: String,
    @SerialName("SchemeName")
    val schemeName: String,
    @SerialName("SecondaryIdentification")
    val secondaryIdentification: String
)

@Serializable
data class ServicerDto(
    @SerialName("Identification")
    val identification: String,
    @SerialName("SchemeName")
    val schemeName: String
)
