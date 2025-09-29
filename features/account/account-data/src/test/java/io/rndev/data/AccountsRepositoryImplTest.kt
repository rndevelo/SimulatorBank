package io.rndev.data

import io.rndev.data.model.AccountDto
import io.rndev.domain.Account
import io.rndev.domain.AccountsException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class AccountsRepositoryImplTest {
    @Mock
    private lateinit var mockDataSource: AccountRemoteDataSource
    private lateinit var repository: AccountsRepositoryImpl

    @Before
    fun setUp() {
        repository = AccountsRepositoryImpl(mockDataSource)
    }

    @Test
    fun `getAccounts success - data source returns list of AccountDto - should return mapped list of Account`() =
        runTest {
            // Given
            val accountDto1 = AccountDto(
                accountId = "1",
                accountType = "SAVINGS",
                accountSubType = "S1",
                currency = "EUR",
                description = "Savings Account",
                nickname = "My Savings",
                openingDate = "2023-01-01",
                balance = "1000.00"
            )
            val accountDto2 = AccountDto(
                accountId = "2",
                accountType = "CURRENT",
                accountSubType = "C1",
                currency = "EUR",
                description = "Current Account",
                nickname = "Main Account",
                openingDate = "2022-01-01",
                balance = "500.50"
            )
            val dtoList = listOf(accountDto1, accountDto2)

            val expectedAccount1 = Account(
                id = "1",
                type = "SAVINGS",
                subType = "S1",
                currency = "EUR",
                description = "Savings Account",
                nickname = "My Savings",
                openingDate = "2023-01-01",
                balance = "1000.00"
            )
            val expectedAccount2 = Account(
                id = "2",
                type = "CURRENT",
                subType = "C1",
                currency = "EUR",
                description = "Current Account",
                nickname = "Main Account",
                openingDate = "2022-01-01",
                balance = "500.50"
            )
            val expectedDomainList = listOf(expectedAccount1, expectedAccount2)

            whenever(mockDataSource.getAccounts()).thenReturn(Result.success(dtoList))

            // When
            val result = repository.getAccounts()

            // Then
            assertTrue(result.isSuccess)
            assertEquals(expectedDomainList, result.getOrNull())
            verify(mockDataSource).getAccounts() // Verify interaction
            verifyNoMoreInteractions(mockDataSource)
        }

    @Test
    fun `getAccounts success - data source returns empty list - should return empty list`() =
        runTest {
            // Given
            val dtoList = emptyList<AccountDto>()
            whenever(mockDataSource.getAccounts()).thenReturn(Result.success(dtoList))

            // When
            val result = repository.getAccounts()

            // Then
            assertTrue(result.isSuccess)
            assertTrue(result.getOrNull()?.isEmpty() == true)
        }

    @Test
    fun `getAccounts failure - data source returns AccountsException - should propagate AccountsException`() =
        runTest {
            // Given
            val expectedException =
                AccountsException.NetworkError(RuntimeException("No connection"))
            whenever(mockDataSource.getAccounts()).thenReturn(Result.failure(expectedException))

            // When
            val result = repository.getAccounts()

            // Then
            assertTrue(result.isFailure)
            assertEquals(expectedException, result.exceptionOrNull())
        }

    @Test
    fun `getAccounts failure - data source returns generic Exception - should propagate generic Exception`() =
        runTest {
            // Given
            val genericException = RuntimeException("Some unexpected error from data source")
            whenever(mockDataSource.getAccounts()).thenReturn(Result.failure(genericException))

            // When
            val result = repository.getAccounts()

            // Then
            assertTrue(result.isFailure)
            assertEquals(genericException, result.exceptionOrNull())
        }

    @Test
    fun `getAccounts failure - mapping DTO with null description - should map correctly`() =
        runTest {
            // Given
            val accountDtoWithNullDesc = AccountDto(
                accountId = "3",
                accountType = "LOAN",
                accountSubType = "L1",
                currency = "USD",
                description = null,
                nickname = "My Loan",
                openingDate = "2023-05-01",
                balance = "-2000.00"
            )
            val dtoList = listOf(accountDtoWithNullDesc)
            val expectedAccount = Account(
                id = "3",
                type = "LOAN",
                subType = "L1",
                currency = "USD",
                description = null,
                nickname = "My Loan",
                openingDate = "2023-05-01",
                balance = "-2000.00"
            )
            whenever(mockDataSource.getAccounts()).thenReturn(Result.success(dtoList))

            // When
            val result = repository.getAccounts()

            // Then
            assertTrue(result.isSuccess)
            assertEquals(listOf(expectedAccount), result.getOrNull())
        }

    @Test
    fun `getAccounts failure - data source returns non-Exception Throwable - should wrap in AccountsException UnknownError`() =
        runTest {
            // Given
            val causeThrowable = object : Throwable("Some low-level error, not an Exception") {}
            whenever(mockDataSource.getAccounts()).thenReturn(Result.failure(causeThrowable))

            // When
            val result = repository.getAccounts()

            // Then
            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is AccountsException.ReadAccountsError)
            assertEquals(
                "Error al procesar la lista de cuentas.",
                (exception as AccountsException.ReadAccountsError).message
            )
//            assertEquals(causeThrowable, exception.cause)
        }
}
