package io.rndev.data

import io.rndev.domain.AccountsRepository
import io.rndev.domain.Account
import io.rndev.data.model.AccountDto // Assuming AccountDto is in this package
import io.rndev.domain.AccountsException // For recoverCatching, as in AuthRepositoryImpl
import javax.inject.Inject

class AccountsRepositoryImpl @Inject constructor(
    private val dataSource: AccountRemoteDataSource
) : AccountsRepository {

    override suspend fun getAccounts(): Result<List<Account>> {
        val resultAccountDtoList: Result<List<AccountDto>> = dataSource.getAccounts()

        return resultAccountDtoList.mapCatching { accountDtoList ->
            // Map List<AccountDto> to List<Account>
            accountDtoList.map { dto ->
                Account(
                    id = dto.accountId,
                    type = dto.accountType,
                    subType = dto.accountSubType,
                    currency = dto.currency,
                    description = dto.description,
                    nickname = dto.nickname,
                    openingDate = dto.openingDate,
                    balance = dto.balance, // Ensure AccountDto.balance is compatible with Account.balance
                )
            }
        }.recoverCatching { throwable ->
            // Following the pattern from AuthRepositoryImpl.kt
            // This will propagate exceptions from dataSource (if they are Exception type)
            // or from the mapping block.
            if (throwable is Exception) {
                // If you need to map data-layer specific exceptions (e.g., io.rndev.data.NetworkException)
                // to domain-layer exceptions (io.rndev.domain.NetworkException), this is where you'd do it.
                // For now, rethrowing as per the AuthRepositoryImpl example's simple propagation.
                throw throwable
            } else {
                // Wrap non-Exception throwables with a domain-specific error
                throw AccountsException.UnknownError(
                    "Error al procesar la lista de cuentas.",
                    throwable
                )
            }
        }
    }
}
