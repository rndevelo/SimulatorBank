package io.rndev.data

import io.rndev.data.model.AccountDto
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(private val api: AccountApiService) :
    AccountRemoteDataSource {

    override suspend fun getAccounts() = api.getAccounts().data.account
}

interface AccountRemoteDataSource {
    suspend fun getAccounts(): List<AccountDto>
}

