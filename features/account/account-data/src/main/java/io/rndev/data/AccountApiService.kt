package io.rndev.data

import io.rndev.data.model.AccountsResponseDto
import retrofit2.http.GET

interface AccountApiService {

    @GET("accounts")
    suspend fun getAccounts(): AccountsResponseDto
}
