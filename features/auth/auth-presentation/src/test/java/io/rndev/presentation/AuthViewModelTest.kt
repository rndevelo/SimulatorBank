package io.rndev.presentation

import io.rndev.domain.AuthException
import io.rndev.domain.AuthUseCase
import io.rndev.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    // Mock para AuthUseCase
    private lateinit var mockAuthUseCase: AuthUseCase

    // Instancia del ViewModel a testear
    private lateinit var viewModel: AuthViewModel

    // Test dispatcher para controlar las coroutines
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Establecer el dispatcher principal para las coroutines de test
        Dispatchers.setMain(testDispatcher)
        mockAuthUseCase = mock()
        viewModel = AuthViewModel(mockAuthUseCase)
    }

    @After
    fun tearDown() {
        // Resetear el dispatcher principal después de los tests
        Dispatchers.resetMain()
    }

    @Test
    fun `onLoginClicked with valid credentials - emits Loading then Success`() = runTest(testDispatcher) {
        // Given
        val email = "test@example.com"
        val password = "password"
        val mockUser = User(id = "1", username = email, name = "Test User")
        val expectedEvents = listOf(
            AuthUiEvent.Loading,
            AuthUiEvent.Success(mockUser)
        )
        val actualEvents = mutableListOf<AuthUiEvent>()

        // Mockear AuthUseCase para que devuelva éxito
        whenever(mockAuthUseCase.invoke(email, password)).thenReturn(Result.success(mockUser))

        val collectionJob = launch {
            viewModel.events.take(expectedEvents.size).toList(actualEvents)
        }

        // When
        viewModel.onLoginClicked(email, password)
        collectionJob.join() // Esperar a que se recolecten los eventos esperados

        // Then
        assertEquals("El número de eventos recolectados debe ser el esperado", expectedEvents.size, actualEvents.size)
        assertEquals(expectedEvents[0], actualEvents[0]) // Loading
        assertEquals(expectedEvents[1], actualEvents[1]) // Success

        verify(mockAuthUseCase).invoke(email, password)
    }

    @Test
    fun `onLoginClicked with invalid credentials - emits Loading then Error`() = runTest(testDispatcher) {
        // Given
        val email = "wrong@example.com"
        val password = "wrongpassword"
        val authException = AuthException.InvalidCredentials
        val expectedErrorMessage = authException.message ?: "Error desconocido"

        val expectedEvents = listOf(
            AuthUiEvent.Loading,
            AuthUiEvent.Error(expectedErrorMessage)
        )
        val actualEvents = mutableListOf<AuthUiEvent>()

        whenever(mockAuthUseCase.invoke(email, password)).thenReturn(Result.failure(authException))

        // When
         val collectionJob = launch {
            viewModel.events.take(expectedEvents.size).toList(actualEvents)
        }
        viewModel.onLoginClicked(email, password)
        collectionJob.join()

        // Then
        assertEquals("El número de eventos recolectados debe ser el esperado", expectedEvents.size, actualEvents.size)
        assertEquals(expectedEvents[0], actualEvents[0]) // Loading
        assertTrue("El segundo evento debería ser de Error", actualEvents[1] is AuthUiEvent.Error)
        assertEquals(expectedEvents[1], actualEvents[1]) // Error

        verify(mockAuthUseCase).invoke(email, password)
    }

    @Test
    fun `onLoginClicked - AuthUseCase throws generic exception - emits Loading then Error with generic message`() = runTest(testDispatcher) {
        // Given
        val email = "error@example.com"
        val password = "errorpassword"
        val genericException = RuntimeException("Network timeout")
        val expectedErrorMessage = genericException.message ?: "Error desconocido"

         val expectedEvents = listOf(
            AuthUiEvent.Loading,
            AuthUiEvent.Error(expectedErrorMessage)
        )
        val actualEvents = mutableListOf<AuthUiEvent>()

        whenever(mockAuthUseCase.invoke(email, password)).thenReturn(Result.failure(genericException))

        // When
        val collectionJob = launch {
            viewModel.events.take(expectedEvents.size).toList(actualEvents)
        }
        viewModel.onLoginClicked(email, password)
        collectionJob.join()

        // Then
        assertEquals(expectedEvents.size, actualEvents.size)
        assertEquals(expectedEvents[0], actualEvents[0])
        assertEquals(expectedEvents[1], actualEvents[1])

        verify(mockAuthUseCase).invoke(email, password)
    }
}
