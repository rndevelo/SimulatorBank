package io.rndev.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Transaction
import io.rndev.detail.domain.usecases.GetAccountUseCase
import io.rndev.detail.domain.usecases.GetBalancesUseCase
import io.rndev.detail.domain.usecases.GetTransactionsUseCase
import kotlinx.coroutines.async
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
        loadAccountDetailProgressive()
    }

    fun loadAccountDetail() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            val accountDeferred = async { getAccountUseCase(accountId) }
            val balancesDeferred = async { getBalancesUseCase(accountId) }
            val transactionsDeferred = async { getTransactionsUseCase(accountId) }

            val account = accountDeferred.await()
            val balances = balancesDeferred.await()
            val transactions = transactionsDeferred.await()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    account = account,
                    balances = balances,
                    transactions = transactions,
                    error = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun refresh() {
        loadAccountDetailProgressive()
    }

    private fun loadAccountDetailProgressive() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            val account = getAccountUseCase(accountId)
            _uiState.update {
                it.copy(account = account)
            }

            val balancesDeferred = async { getBalancesUseCase(accountId) }
            val transactionsDeferred = async { getTransactionsUseCase(accountId) }

            val balances = balancesDeferred.await()
            val transactions = transactionsDeferred.await()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    balances = balances,
                    transactions = transactions,
                    error = null
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido"
                )
            }
        }
    }
}
