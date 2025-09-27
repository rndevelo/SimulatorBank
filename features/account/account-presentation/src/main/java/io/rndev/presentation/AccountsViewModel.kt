package io.rndev.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rndev.domain.Account
import io.rndev.domain.GetAccountsUseCase
import io.rndev.domain.AccountsException // Asumiendo que esta es la clase de excepción relevante del dominio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountUiState(
    val isLoading: Boolean = true, // Valor inicial por defecto
    val accounts: List<Account> = emptyList(),
    val error: String? = null,
)

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    private val _accountUiState = MutableStateFlow(AccountUiState()) // Estado inicial con isLoading = true
    val accountUiState: StateFlow<AccountUiState> = _accountUiState.asStateFlow()

    init {
        loadAccounts()
    }

    fun loadAccounts() {
        viewModelScope.launch {
            _accountUiState.update {
                it.copy(isLoading = true, error = null) // Resetear error en cada carga
            }

            getAccountsUseCase()
                .onSuccess { fetchedAccounts ->
                    Log.d("AccountsViewModel", "loadAccounts successful: ${fetchedAccounts.size} accounts")
                    _accountUiState.update {
                        it.copy(
                            isLoading = false,
                            accounts = fetchedAccounts,
                            error = null
                        )
                    }
                }
                .onFailure { throwable ->
                    Log.e("AccountsViewModel", "loadAccounts failed", throwable)
                    // Aquí podrías tener un mapeo de errores más sofisticado como en AuthViewModel
                    // Por ahora, usamos el mensaje del throwable o un mensaje genérico.
                    val errorMessage = when (throwable) {
                        is AccountsException.InvalidCredentials -> "Error de credenciales (aunque raro para cuentas)"
                        is AccountsException.NetworkError -> "Error de red. Revisa tu conexión."
                        is AccountsException.ServerError -> "Error del servidor (${throwable.errorCode}). Intenta más tarde."
                        is AccountsException.UserNotFound -> "Usuario no encontrado (raro para cuentas)"
                        else -> throwable.localizedMessage ?: "Ocurrió un error desconocido al cargar las cuentas."
                    }
                    _accountUiState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage,
                            accounts = emptyList() // Mantener la lista vacía en caso de error
                        )
                    }
                }
        }
    }
}
