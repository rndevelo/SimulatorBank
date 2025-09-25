package io.rndev.detail.data

import io.rndev.detail.data.model.AccountResponseDto
import io.rndev.detail.data.model.BalancesResponseDto
import io.rndev.detail.data.model.PartyResponseDto
import io.rndev.detail.data.model.SandboxRequestDto
import io.rndev.detail.data.model.SandboxResponseDto
import io.rndev.detail.data.model.TransactionsResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DetailApiService {

    @GET("accounts/{accountId}")
    suspend fun getAccount(@Path("accountId") accountId: String): AccountResponseDto

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
