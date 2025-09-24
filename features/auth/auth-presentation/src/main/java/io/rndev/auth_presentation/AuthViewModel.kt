package io.rndev.auth_presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rndev.auth_domain.AuthException
import io.rndev.auth_domain.AuthUseCase
import io.rndev.auth_domain.User
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface AuthUiEvent {
    object Idle : AuthUiEvent // Estado inicial, antes de cualquier operación
    object Loading : AuthUiEvent // Cargando datos (ej. durante el login)
    data class Success(val user: User) : AuthUiEvent // Login exitoso
    data class Error(val error: String) : AuthUiEvent // Ocurrió un error
}

@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _eventChannel = Channel<AuthUiEvent>()
    val events = _eventChannel.receiveAsFlow()

    fun onLoginClicked(email: String, password: String) {
        viewModelScope.launch {

            _eventChannel.send(AuthUiEvent.Loading)

            authUseCase(email, password) // Asumiendo que AuthUseCase es suspend operator fun invoke(email, pass)
                .onSuccess { user ->
                    _eventChannel.send(AuthUiEvent.Success(user))
                }
                .onFailure { throwable ->
                    val authError = throwable as? AuthException
                        ?: AuthException.UnknownError("Error inesperado en ViewModel", throwable)
                    val userFriendlyMessage = getUserFriendlyMessage(authError)
                    _eventChannel.send(AuthUiEvent.Error(userFriendlyMessage))
                }
        }
    }
}

private fun getUserFriendlyMessage(exception: AuthException): String {
    return when (exception) {
        is AuthException.NetworkError -> "Error de red. Revisa tu conexión."
        is AuthException.InvalidCredentials -> "Credenciales incorrectas."
        is AuthException.ServerError -> "Error del servidor (${exception.errorCode}). Intenta más tarde."
        else -> exception.message ?: "Ocurrió un error desconocido."
    }
}
