package io.rndev.detail.data

import io.rndev.detail.data.model.AccountResponseDto
import io.rndev.detail.data.model.BalancesResponseDto
import io.rndev.detail.data.model.PartyResponseDto
import io.rndev.detail.data.model.SandboxRequestDto
import io.rndev.detail.data.model.SandboxResponseDto
import io.rndev.detail.data.model.TransactionsResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DetailApiService {

    @GET("accounts/{accountId}")
    suspend fun getAccount(@Path("accountId") accountId: String): Response<AccountResponseDto>

    @GET("accounts/{accountId}/transactions")
    suspend fun getTransactions(@Path("accountId") accountId: String): Response<TransactionsResponseDto>

    @GET("accounts/{accountId}/balances")
    suspend fun getBalances(@Path("accountId") accountId: String): Response<BalancesResponseDto>

//    @GET("account-access-consents/{consentId}")
//    suspend fun getAccountConsent(@Path("consentId") consentId: String): AccountConsentResponseDto

    @POST("sandbox")
    suspend fun sandboxOperation(@Body request: SandboxRequestDto): Response<SandboxResponseDto>

    @GET("party")
    suspend fun getParty(): Response<PartyResponseDto>
}



//Last api
//
//
//interface AuthService {
//
//    @GET("discover/movie?sort_by=popularity.desc")
//    suspend fun fetchPopularMovies(@Query("region") region: String): RemoteResult
//
//    @GET("movie/{id}")
//    suspend fun fetchMovieById(@Path("id") id: Int): RemoteMovie
//
//}
