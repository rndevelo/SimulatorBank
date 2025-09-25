package io.rndev.data

import io.rndev.domain.AccountRepository
import io.rndev.domain.Account
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val dataSource: AccountRemoteDataSource
) : AccountRepository {

    override suspend fun getAccounts(): List<Account> =
        dataSource.getAccounts().map { dto ->
            Account(
                id = dto.accountId,
                type = dto.accountType,
                subType = dto.accountSubType,
                currency = dto.currency,
                description = dto.description,
                nickname = dto.nickname,
                openingDate = dto.openingDate,
                balance = dto.balance,
            )
        }
}
