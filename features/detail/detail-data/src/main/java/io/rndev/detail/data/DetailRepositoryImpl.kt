package io.rndev.detail.data

import io.rndev.detail.domain.DetailRepository
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Transaction
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Party
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val dataSource: DetailRemoteDataSource
) : DetailRepository {

    override suspend fun getAccount(accountId: String): Account =
        dataSource.getAccount(accountId).data.account.let { dto ->
            Account(
                id = dto.accountId,
                type = dto.accountType,
                subType = dto.accountSubType,
                currency = dto.currency,
                description = dto.description,
                nickname = dto.nickname,
                openingDate = dto.openingDate,
                balance = dto.balance
            )
        }

    override suspend fun getTransactions(accountId: String): List<Transaction> =
        dataSource.getTransactions(accountId).map { dto ->
            Transaction(
                transactionId = dto.transactionId, // <-- AÑADIDO AQUÍ
                accountId = dto.accountId,
                amount = dto.amount.amount.toDoubleOrNull() ?: 0.0,
                currency = dto.amount.currency,
                bookingDateTime = dto.bookingDateTime,
                creditDebitIndicator = dto.creditDebitIndicator,
                description = dto.transactionInformation,
                reference = dto.transactionReference
            )
        }

    override suspend fun getBalances(accountId: String): List<Balance> =
        dataSource.getBalances(accountId).map { dto ->
            Balance(
                accountId = dto.accountId,
                amount = dto.amount.amount.toDoubleOrNull() ?: 0.0,
                currency = dto.amount.currency,
                creditDebitIndicator = dto.creditDebitIndicator,
                type = dto.type
            )
        }

    override suspend fun getParty(): Party =
        dataSource.getParty().let { dto ->
            Party(
                id = dto.partyId,
                name = dto.name
            )
        }
}
