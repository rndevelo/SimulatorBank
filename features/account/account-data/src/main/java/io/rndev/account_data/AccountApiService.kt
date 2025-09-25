package io.rndev.account_data

import io.rndev.account_data.model.AccountDto
import io.rndev.account_data.model.AccountsResponseDto
import io.rndev.account_data.model.BalancesResponseDto
import io.rndev.account_data.model.PartyResponseDto
import io.rndev.account_data.model.SandboxRequestDto
import io.rndev.account_data.model.SandboxResponseDto
import io.rndev.account_data.model.TransactionsResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApiService {

    @GET("accounts")
    suspend fun getAccounts(): AccountsResponseDto

    @GET("accounts/{accountId}")
    suspend fun getAccount(@Path("accountId") accountId: String): AccountDto

    @GET("accounts/{accountId}/transactions")
    suspend fun getTransactions(@Path("accountId") accountId: String): TransactionsResponseDto

    @GET("accounts/{accountId}/balances")
    suspend fun getBalances(@Path("accountId") accountId: String): BalancesResponseDto

//    @GET("account-access-consents/{consentId}")
//    suspend fun getAccountConsent(@Path("consentId") consentId: String): AccountConsentResponseDto

    @POST("sandbox")
    suspend fun sandboxOperation(@Body request: SandboxRequestDto): SandboxResponseDto

    @GET("party")
    suspend fun getParty(): PartyResponseDto
}
