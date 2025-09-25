package io.rndev.account_presentation // o io.rndev.account_presentation.accounts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rndev.account_domain.model.Account
import io.rndev.account_domain.usecases.GetAccountsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountUiState(
    val isLoading: Boolean,
    val accounts: List<Account>,
    val error: String?,
)

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase
) : ViewModel() {

    private val _accountUiState =
        MutableStateFlow(
            AccountUiState(
                isLoading = true,
                accounts = emptyList(),
                error = null
            )
        )

    val accountUiState: StateFlow<AccountUiState?> = _accountUiState

    init {
        loadAccounts()
    }

    fun loadAccounts() = viewModelScope.launch {
        _accountUiState.update {
            it.copy(isLoading = true)
        }
        try {
            val accounts = getAccountsUseCase()
            Log.d("AccountsViewModel", "loadAccounts: accounts = $accounts")

            _accountUiState.update {
                it.copy(isLoading = false, accounts = accounts)
            }
        } catch (e: Exception) {
            _accountUiState.update {
                it.copy(isLoading = false, error = e.message)
            }
        }
    }
}


//class TransactionsViewModel(private val getTransactionsUseCase: GetTransactionsUseCase) : ViewModel() {
//    private val _uiState = MutableStateFlow<TransactionsUiState>(TransactionsUiState.Loading)
//    val uiState: StateFlow<TransactionsUiState> = _uiState
//
//    fun loadTransactions(accountId: String) = viewModelScope.launch {
//        try {
//            val txns = getTransactionsUseCase(accountId)
//            _uiState.value = TransactionsUiState.Success(txns)
//        } catch (e: Exception) {
//            _uiState.value = TransactionsUiState.Error(e)
//        }
//    }
//}
//
//class SandboxViewModel(private val sandboxUseCase: SandboxOperationUseCase) : ViewModel() {
//    private val _uiState = MutableStateFlow<SandboxUiState>(SandboxUiState.Idle)
//    val uiState: StateFlow<SandboxUiState> = _uiState
//
//    fun executeOperation(request: SandboxRequest) = viewModelScope.launch {
//        _uiState.value = SandboxUiState.Loading
//        val result = sandboxUseCase(request)
//        _uiState.value = when(result) {
//            is SandboxResult.Success -> SandboxUiState.Success
//            is SandboxResult.Failure -> SandboxUiState.Error(result.message)
//        }
//    }
//}
