package io.rndev.detail.data

import io.rndev.detail.data.model.AccountResponseDto // Necesario para el mapeo intermedio
import io.rndev.detail.domain.DetailRepository
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Party
import io.rndev.detail.domain.model.Transaction
import io.rndev.detail.domain.DetailException
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val dataSource: DetailRemoteDataSource // La interfaz ya devuelve Result<DtoType>
) : DetailRepository {

    override suspend fun getAccount(accountId: String): Result<Account> {
        val resultAccountResponseDto: Result<AccountResponseDto> = dataSource.getAccount(accountId)

        return resultAccountResponseDto.mapCatching { accountResponseDto ->
            // Mapear de AccountResponseDto.data.account a Account (modelo de dominio)
            val dto = accountResponseDto.data.account
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
        }.recoverCatching { throwable ->
            handleThrowable(throwable, DetailException.AccountDataError)
        }
    }

    override suspend fun getTransactions(accountId: String): Result<List<Transaction>> {
        val resultTransactionDtoList = dataSource.getTransactions(accountId)

        return resultTransactionDtoList.mapCatching { transactionDtoList ->
            transactionDtoList.map { dto ->
                Transaction(
                    transactionId = dto.transactionId,
                    accountId = dto.accountId,
                    amount = dto.amount.amount.toDoubleOrNull() ?: 0.0,
                    currency = dto.amount.currency,
                    bookingDateTime = dto.bookingDateTime,
                    creditDebitIndicator = dto.creditDebitIndicator,
                    description = dto.transactionInformation,
                    reference = dto.transactionReference
                )
            }
        }.recoverCatching { throwable ->
            handleThrowable(throwable, DetailException.TransactionsDataError)
        }
    }

    override suspend fun getBalances(accountId: String): Result<List<Balance>> {
        val resultBalanceDtoList = dataSource.getBalances(accountId)

        return resultBalanceDtoList.mapCatching { balanceDtoList ->
            balanceDtoList.map { dto ->
                Balance(
                    accountId = dto.accountId,
                    amount = dto.amount.amount.toDoubleOrNull() ?: 0.0,
                    currency = dto.amount.currency,
                    creditDebitIndicator = dto.creditDebitIndicator,
                    type = dto.type
                )
            }
        }.recoverCatching { throwable ->
            handleThrowable(throwable, DetailException.BalancesDataError)
        }
    }

    override suspend fun getParty(): Result<Party> {
        val resultPartyDto = dataSource.getParty()

        return resultPartyDto.mapCatching { dto ->
            Party(
                id = dto.partyId,
                name = dto.name
            )
        }.recoverCatching { throwable ->
            handleThrowable(throwable, DetailException.PartyDataError)
        }
    }

    // Función de ayuda para manejar throwables de forma consistente
    private fun handleThrowable(throwable: Throwable, exception: Exception): Nothing {
        if (throwable is Exception) {
            throw throwable // Propaga excepciones conocidas (incluyendo NetworkException del dataSource)
        } else {
            throw exception
        }
    }
}
