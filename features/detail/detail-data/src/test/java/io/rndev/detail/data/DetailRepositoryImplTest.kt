package io.rndev.detail.data

import io.rndev.detail.data.model.AccountDataDto
import io.rndev.detail.data.model.AccountResponseDto
import io.rndev.detail.data.model.AmountDto
import io.rndev.detail.data.model.BalanceDto
import io.rndev.detail.data.model.PartyDto
import io.rndev.detail.data.model.TransactionDto
import io.rndev.detail.domain.DetailException
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Party
import io.rndev.detail.domain.model.Transaction
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.*
import io.rndev.detail.data.model.AccountDto as DetailAccountDto

class DetailRepositoryImplTest {

    @Mock
    private lateinit var mockDataSource: DetailRemoteDataSource
    private lateinit var repository: DetailRepositoryImpl

    private val accountId = "testAccountId"

    @Before
    fun setUp() {
        mockDataSource = mock()
        repository = DetailRepositoryImpl(mockDataSource)
    }

    // --- getAccount Tests ---

    @Test
    fun `getAccount success - maps DTO to domain Account`() = runTest {
        // Given
        val detailAccountDto = DetailAccountDto(
            accountId = accountId,
            accountType = "CURRENT",
            accountSubType = "C1",
            currency = "EUR",
            description = "Test Account Desc",
            nickname = "Test Nickname",
            openingDate = "2023-01-01",
            balance = "123.45"
        )
        val accountResponseDto =
            AccountResponseDto(data = AccountDataDto(account = detailAccountDto))
        val expectedAccount = Account(
            id = accountId,
            type = "CURRENT",
            subType = "C1",
            currency = "EUR",
            description = "Test Account Desc",
            nickname = "Test Nickname",
            openingDate = "2023-01-01",
            balance = "123.45"
        )

        whenever(mockDataSource.getAccount(accountId)).thenReturn(Result.success(accountResponseDto))

        // When
        val result = repository.getAccount(accountId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedAccount, result.getOrNull())
        verify(mockDataSource).getAccount(accountId)
    }

    @Test
    fun `getAccount failure - dataSource returns specific Exception - propagates Exception`() =
        runTest {
            // Given
            val expectedException = DetailException.NetworkError(RuntimeException("Network issue"))
            whenever(mockDataSource.getAccount(accountId)).thenReturn(
                Result.failure(
                    expectedException
                )
            )

            // When
            val result = repository.getAccount(accountId)

            // Then
            assertTrue(result.isFailure)
            assertEquals(expectedException, result.exceptionOrNull())
        }

    @Test
    fun `getAccount failure - dataSource returns generic Exception - propagates Exception`() =
        runTest {
            // Given
            val genericException = RuntimeException("Some generic error")
            whenever(mockDataSource.getAccount(accountId)).thenReturn(
                Result.failure(
                    genericException
                )
            )

            // When
            val result = repository.getAccount(accountId)

            // Then
            assertTrue(result.isFailure)
            assertEquals(genericException, result.exceptionOrNull())
        }

    @Test
    fun `getAccount failure - dataSource returns Throwable - wraps in UnknownError`() = runTest {
        // Given
        val causeThrowable = object : Throwable("Low level issue") {}
        whenever(mockDataSource.getAccount(accountId)).thenReturn(Result.failure(causeThrowable))

        // When
        val result = repository.getAccount(accountId)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DetailException.UnknownError)
        assertEquals(
            "Error al procesar los datos de la cuenta.",
            (exception as DetailException.AccountDataError).message
        )
        assertEquals(causeThrowable, exception.cause)
    }


    // --- getTransactions Tests ---

    @Test
    fun `getTransactions success - maps DTO list to domain Transaction list`() = runTest {
        // Given
        val transactionDto1 = TransactionDto(
            transactionId = "tx1", accountId = accountId, amount = AmountDto("100.50", "EUR"),
            bookingDateTime = "2023-01-01T10:00:00Z", creditDebitIndicator = "Credit",
            status = "Booked", transactionReference = "Ref1", transactionInformation = "Info1"
        )
        val transactionDto2 = TransactionDto( // Test with invalid amount string
            transactionId = "tx2",
            accountId = accountId,
            amount = AmountDto("invalidAmount", "USD"),
            bookingDateTime = "2023-01-02T11:00:00Z",
            creditDebitIndicator = "Debit",
            status = "Pending",
            transactionReference = "Ref2",
            transactionInformation = "Info2"
        )
        val dtoList = listOf(transactionDto1, transactionDto2)
        val expectedTransaction1 = Transaction(
            transactionId = "tx1", accountId = accountId, amount = 100.50, currency = "EUR",
            bookingDateTime = "2023-01-01T10:00:00Z", creditDebitIndicator = "Credit",
            description = "Info1", reference = "Ref1"
        )
        val expectedTransaction2 = Transaction( // Amount defaults to 0.0
            transactionId = "tx2", accountId = accountId, amount = 0.0, currency = "USD",
            bookingDateTime = "2023-01-02T11:00:00Z", creditDebitIndicator = "Debit",
            description = "Info2", reference = "Ref2"
        )
        whenever(mockDataSource.getTransactions(accountId)).thenReturn(Result.success(dtoList))

        // When
        val result = repository.getTransactions(accountId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf(expectedTransaction1, expectedTransaction2), result.getOrNull())
    }

    @Test
    fun `getTransactions success - empty list from dataSource`() = runTest {
        // Given
        whenever(mockDataSource.getTransactions(accountId)).thenReturn(Result.success(emptyList()))

        // When
        val result = repository.getTransactions(accountId)

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getTransactions failure - dataSource returns Exception - propagates Exception`() =
        runTest {
            // Given
            val expectedException = DetailException.ServerError(500, "Server meltdown")
            whenever(mockDataSource.getTransactions(accountId)).thenReturn(
                Result.failure(
                    expectedException
                )
            )

            // When
            val result = repository.getTransactions(accountId)

            // Then
            assertTrue(result.isFailure)
            assertEquals(expectedException, result.exceptionOrNull())
        }

    @Test
    fun `getTransactions failure - dataSource returns Throwable - wraps in UnknownError`() =
        runTest {
            // Given
            val causeThrowable = object : Throwable("Another low level issue") {}
            whenever(mockDataSource.getTransactions(accountId)).thenReturn(
                Result.failure(
                    causeThrowable
                )
            )

            // When
            val result = repository.getTransactions(accountId)

            // Then
            assertTrue(result.isFailure)
            val exception = result.exceptionOrNull()
            assertTrue(exception is DetailException.UnknownError)
            assertEquals(
                "Error al procesar la lista de transacciones.",
                (exception as DetailException.TransactionsDataError).message
            )
            assertEquals(causeThrowable, exception.cause)
        }

    // --- getBalances Tests ---

    @Test
    fun `getBalances success - maps DTO list to domain Balance list`() = runTest {
        // Given
        val balanceDto1 = BalanceDto(
            accountId = accountId,
            amount = AmountDto("2000.75", "EUR"),
            creditDebitIndicator = "Credit",
            type = "ClosingBooked",
            dateTime = "2023-01-01T10:00:00Z"
        )
        val balanceDto2 = BalanceDto( // Test with invalid amount string
            accountId = accountId,
            amount = AmountDto("badAmount", "USD"),
            creditDebitIndicator = "Debit",
            type = "InterimAvailable",
            dateTime = "2023-01-02T11:00:00Z"
        )
        val dtoList = listOf(balanceDto1, balanceDto2)
        val expectedBalance1 = Balance(
            accountId = accountId, amount = 2000.75, currency = "EUR",
            creditDebitIndicator = "Credit", type = "ClosingBooked"
        )
        val expectedBalance2 = Balance( // Amount defaults to 0.0
            accountId = accountId, amount = 0.0, currency = "USD",
            creditDebitIndicator = "Debit", type = "InterimAvailable"
        )
        whenever(mockDataSource.getBalances(accountId)).thenReturn(Result.success(dtoList))

        // When
        val result = repository.getBalances(accountId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(listOf(expectedBalance1, expectedBalance2), result.getOrNull())
    }

    @Test
    fun `getBalances failure - dataSource returns Exception - propagates Exception`() = runTest {
        // Given
        val expectedException = DetailException.InvalidCredentials
        whenever(mockDataSource.getBalances(accountId)).thenReturn(Result.failure(expectedException))

        // When
        val result = repository.getBalances(accountId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }

    @Test
    fun `getBalances failure - dataSource returns Throwable - wraps in UnknownError`() = runTest {
        // Given
        val causeThrowable = object : Throwable("DB connection failed") {}
        whenever(mockDataSource.getBalances(accountId)).thenReturn(Result.failure(causeThrowable))

        // When
        val result = repository.getBalances(accountId)

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DetailException.UnknownError)
        assertEquals(
            "Error al procesar la lista de saldos.",
            (exception as DetailException.BalancesDataError).message
        )
        assertEquals(causeThrowable, exception.cause)
    }

    // --- getParty Tests ---

    @Test
    fun `getParty success - maps DTO to domain Party`() = runTest {
        // Given
        val partyDto = PartyDto(partyId = "party1", name = "John Doe")
        val expectedParty = Party(id = "party1", name = "John Doe")

        whenever(mockDataSource.getParty()).thenReturn(Result.success(partyDto))

        // When
        val result = repository.getParty()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedParty, result.getOrNull())
    }

    @Test
    fun `getParty failure - dataSource returns Exception - propagates Exception`() = runTest {
        // Given
        val expectedException = DetailException.UserNotFound
        whenever(mockDataSource.getParty()).thenReturn(Result.failure(expectedException))

        // When
        val result = repository.getParty()

        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }

    @Test
    fun `getParty failure - dataSource returns Throwable - wraps in UnknownError`() = runTest {
        // Given
        val causeThrowable = object : Throwable("Party system offline") {}
        whenever(mockDataSource.getParty()).thenReturn(Result.failure(causeThrowable))

        // When
        val result = repository.getParty()

        // Then
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is DetailException.PartyDataError)
        assertEquals(
            "Error al procesar los datos del titular.",
            (exception as DetailException.PartyDataError).message
        )
        assertEquals(causeThrowable, exception.cause)
    }
}
