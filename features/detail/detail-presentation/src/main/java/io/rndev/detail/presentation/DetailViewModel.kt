package io.rndev.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rndev.detail.domain.DetailException
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Transaction
import io.rndev.detail.domain.usecases.GetAccountUseCase
import io.rndev.detail.domain.usecases.GetBalancesUseCase
import io.rndev.detail.domain.usecases.GetTransactionsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = true,
    val account: Account? = null,
    val balances: List<Balance> = emptyList(),
    val transactions: List<Transaction> = emptyList(),
    val error: String? = null
)

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
class DetailViewModel @AssistedInject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getBalancesUseCase: GetBalancesUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    @Assisted val accountId: String,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(accountId: String): DetailViewModel
    }

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        // Elige una estrategia de carga inicial, por ejemplo, progresiva
        loadAccountDetails()
    }

    // Combina la lógica de carga, haciéndola más sencilla de llamar (ej. para refresh)
    fun loadAccountDetails() {
        loadAccountDetailProgressiveInternal()
    }

    private fun loadAccountDetailProgressiveInternal() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null, account = null, balances = emptyList(), transactions = emptyList()) }

        // 1. Cargar datos de la cuenta
        val accountResult = getAccountUseCase(accountId)
        accountResult
            .onSuccess { accountData ->
                _uiState.update { it.copy(account = accountData) } // Aún podría estar cargando el resto

                // 2. Si la cuenta se cargó, cargar saldos y transacciones en paralelo
                coroutineScope { // Asegura que ambas tareas finalicen o se cancelen juntas
                    val balancesResultDeferred = async { getBalancesUseCase(accountId) }
                    val transactionsResultDeferred = async { getTransactionsUseCase(accountId) }

                    val balancesResult = balancesResultDeferred.await()
                    val transactionsResult = transactionsResultDeferred.await()

                    var tempError: String? = null

                    balancesResult
                        .onSuccess { balancesData -> _uiState.update { it.copy(balances = balancesData) } }
                        .onFailure { throwable -> tempError = tempError ?: getErrorMessage(throwable) }

                    transactionsResult
                        .onSuccess { transactionsData -> _uiState.update { it.copy(transactions = transactionsData) } }
                        .onFailure { throwable -> tempError = tempError ?: getErrorMessage(throwable) }

                    _uiState.update { it.copy(isLoading = false, error = tempError) }
                }
            }
            .onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = getErrorMessage(throwable)
                    )
                }
            }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is DetailException.InvalidCredentials -> "Error de credenciales."
            is DetailException.NetworkError -> "Error de red. Revisa tu conexión."
            is DetailException.ServerError -> "Error del servidor (${throwable.errorCode}). Intenta más tarde."
            else -> throwable.localizedMessage ?: "Ocurrió un error desconocido."
        }
    }

    fun refresh() {
        loadAccountDetails()
    }
}
