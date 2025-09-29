package io.rndev.data

import io.rndev.data.model.AccountsResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface AccountsApiService {

    @GET("accounts")
    suspend fun getAccounts(): Response<AccountsResponseDto>
}
