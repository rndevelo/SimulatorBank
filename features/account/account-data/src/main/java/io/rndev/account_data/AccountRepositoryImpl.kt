package io.rndev.account_data

import io.rndev.account_domain.AccountRepository
import io.rndev.account_domain.model.Account
import io.rndev.account_domain.model.Balance
import io.rndev.account_domain.model.Party
import io.rndev.account_domain.model.Transaction
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

    override suspend fun getAccount(accountId: String): Account =
        dataSource.getAccount(accountId).let { dto ->
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

    override suspend fun getTransactions(accountId: String): List<Transaction> =
        dataSource.getTransactions(accountId).map { dto ->
            Transaction(
                accountId = dto.accountId,
                amount = dto.amount,
                currency = dto.currency,
                bookingDateTime = dto.bookingDateTime,
                creditDebitIndicator = dto.creditDebitIndicator,
                description = dto.description,
                reference = dto.transactionReference
            )
        }

    override suspend fun getBalances(accountId: String): List<Balance> =
        dataSource.getBalances(accountId).map { dto ->
            Balance(
                accountId = dto.accountId,
                amount = dto.amount,
                currency = dto.currency,
                creditDebitIndicator = dto.creditDebitIndicator
            )
        }

    override suspend fun getParty(): Party =
        dataSource.getParty().let { dto ->
            Party(id = dto.partyId, name = dto.name)
        }

//    override suspend fun sandboxOperation(request: SandboxRequest): SandboxResult =
//        dataSource.sandboxOperation(SandboxRequestDto(request.accountId, request.operation, request.amount))
//            .let { resp ->
//                if (resp.sandboxId.isNotEmpty()) SandboxResult.Success else SandboxResult.Failure(resp.message ?: "Error")
//            }
}
