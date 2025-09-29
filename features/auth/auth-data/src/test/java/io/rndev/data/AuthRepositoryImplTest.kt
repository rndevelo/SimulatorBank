package io.rndev.data

import io.rndev.core.common.TokenProvider
import io.rndev.domain.AuthException
import io.rndev.domain.User // Dominio User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

// AuthDto y UserDto están definidos en io.rndev.data.LoginRequest.kt
// por lo que están en el mismo paquete y no necesitan import explícito aquí,
// pero si estuvieran en un subpaquete como io.rndev.data.model, se importarían.
@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryImplTest {

    @Mock
    private lateinit var mockAuthRemoteDataSource: AuthRemoteDataSource
    @Mock
    private lateinit var mockTokenProvider: TokenProvider
    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setUp() {
        authRepository = AuthRepositoryImpl(mockAuthRemoteDataSource, mockTokenProvider)
    }

    @Test
    fun `login with valid credentials - remote success - should save token and return User`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val userDto = UserDto(id = "user1", username = username, name = "Test User")
        val authDto = AuthDto(accessToken = "mock_access_token", refreshToken = "mock_refresh_token", user = userDto)
        val expectedUser = User(id = "user1", username = username, name = "Test User")

        whenever(mockAuthRemoteDataSource.login(username, password))
            .thenReturn(Result.success(authDto))

        // When
        val result = authRepository.login(username, password)

        // Then
        verify(mockTokenProvider).saveToken("mock_access_token")
        assertTrue(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
        verifyNoMoreInteractions(mockTokenProvider) // Asegura que no se hicieron más llamadas inesperadas
    }

    @Test
    fun `login with invalid credentials - remote returns AuthException - should propagate failure`() = runTest {
        // Given
        val username = "wronguser"
        val password = "wrongpassword"
        val expectedException = AuthException.InvalidCredentials

        whenever(mockAuthRemoteDataSource.login(username, password))
            .thenReturn(Result.failure(expectedException))

        // When
        val result = authRepository.login(username, password)

        // Then
        verify(mockTokenProvider, never()).saveToken(any())
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }

    @Test
    fun `login - remote returns generic Exception - should propagate failure`() = runTest {
        // Given
        val username = "user"
        val password = "pw"
        val genericException = RuntimeException("Network error")

        whenever(mockAuthRemoteDataSource.login(username, password))
            .thenReturn(Result.failure(genericException))

        // When
        val result = authRepository.login(username, password)

        // Then
        verify(mockTokenProvider, never()).saveToken(any())
        assertTrue(result.isFailure)
        assertEquals(genericException, result.exceptionOrNull())
    }

    @Test
    fun `login success from remote - but saveToken fails - should propagate failure from saveToken`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        val userDto = UserDto(id = "user1", username = username, name = "Test User")
        val authDto = AuthDto(accessToken = "token_that_will_fail_saving", refreshToken = "refresh", user = userDto)
        val saveTokenException = RuntimeException("Disk full or permission denied")

        whenever(mockAuthRemoteDataSource.login(username, password))
            .thenReturn(Result.success(authDto))
        whenever(mockTokenProvider.saveToken("token_that_will_fail_saving"))
            .thenThrow(saveTokenException)

        // When
        val result = authRepository.login(username, password)

        // Then
        verify(mockTokenProvider).saveToken("token_that_will_fail_saving") // Se intentó guardar
        assertTrue(result.isFailure)
        assertEquals(saveTokenException, result.exceptionOrNull())
    }

    @Test
    fun `login success from remote - but DTO to domain mapping fails (e_g_, NPE) - should propagate failure`() = runTest {
        // Given
        val username = "testuser"
        val password = "password123"
        // Crear un AuthDto que cause un NullPointerException en toDomainModel()
        // por ejemplo, si 'user' es null y se intenta acceder a user.id
        val faultyAuthDto = AuthDto(accessToken = "valid_token", refreshToken = "refresh", user = null)

        whenever(mockAuthRemoteDataSource.login(username, password))
            .thenReturn(Result.success(faultyAuthDto))

        // When
        val result = authRepository.login(username, password)

        // Then
        // saveToken se llama ANTES del mapeo en el código actual.
        // Si el mapeo falla, el token ya se habría intentado guardar si el `mapCatching` lo permite.
        // En tu código actual: requestDto.mapCatching { authDto -> tokenProvider.saveToken(); authDto.toDomainModel() }
        // La llamada a saveToken está DENTRO del mapCatching, por lo que si el DTO es inválido de forma que
        // `accessToken` es null y saveToken lo requiere non-null, o si `user` es null causando NPE en `authDto.user`
        // antes de llamar a toDomainModel() en sí mismo.
        // En este caso, si `faultyAuthDto.user` es null, `authDto.toDomainModel()` causará el NPE.
        // `tokenProvider.saveToken(faultyAuthDto.accessToken)` se llamaría primero.
        verify(mockTokenProvider).saveToken("valid_token") // Se intentó guardar el token
        assertTrue(result.isFailure)
        assertTrue("Expected NullPointerException due to null UserDto", result.exceptionOrNull() is NullPointerException)
    }

    @Test
    fun `login failure - data source returns non-AuthException Throwable - should wrap in AuthException UnknownError`() = runTest {
        // Given
        val username = "testuser"
        val password = "password"
        val causeThrowable = object : Throwable("Some low-level error") {} // No es una Exception

        whenever(mockAuthRemoteDataSource.login(username, password))
            .thenReturn(Result.failure(causeThrowable))

        // When
        val result = authRepository.login(username, password)

        // Then
        verify(mockTokenProvider, never()).saveToken(any())
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is AuthException.NetworkError)
        assertEquals("Error al procesar la respuesta del servidor.", (exception as AuthException.ReadServerResponseError).message)
        assertEquals(causeThrowable, exception.cause)
    }
}
