package io.rndev.auth_presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rndev.auth_domain.AuthUseCase
import io.rndev.auth_domain.User
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = authUseCase(username, password)

                Log.d("UserAuthResult", "result: $result")

                _uiState.value = _uiState.value.copy(user = result)
//                if (result.isSuccess) {
//                    onSuccess()
//                } else {
//                    _uiState.value = _uiState.value.copy(error = result.exceptionOrNull()?.message)
//                }
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(error = e.localizedMessage ?: "Error desconocido")
                Log.d("UserAuthResult", "error: ${e.localizedMessage}")
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}
