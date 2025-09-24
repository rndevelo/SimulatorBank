package io.rndev.auth_presentation // O el paquete que uses para tu UI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

enum class Loading {
    Show, Hide
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccessNavigation: () -> Unit = {} // Callback para navegar tras login exitoso
) {

    val snackBarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(Loading.Hide) }

    LaunchedEffect(Unit) {
        authViewModel.events.collect { event ->
            when (event) {
                is AuthUiEvent.Idle -> TODO()
                is AuthUiEvent.Loading -> isLoading = Loading.Show
                is AuthUiEvent.Success -> {
                    isLoading = Loading.Hide
                    snackBarHostState.showSnackbar(
                        message = event.user.toString(),
                        duration = SnackbarDuration.Long
                    )
                }

                is AuthUiEvent.Error -> {
                    isLoading = Loading.Hide
                    snackBarHostState.showSnackbar(
                        message = event.error,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    AuthContent(snackBarHostState, modifier, isLoading, authViewModel)
}

@Composable
private fun AuthContent(
    snackBarHostState: SnackbarHostState,
    modifier: Modifier,
    isLoading: Loading,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Login", style = MaterialTheme.typography.headlineMedium)

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
//                    enabled = uiState !is AuthUiEvent.Loading // Deshabilitar si está cargando
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
//                    enabled = uiState !is AuthUiEvent.Loading // Deshabilitar si está cargando
                )

                // El botón de login y el indicador de carga se manejan según el estado
                when (isLoading) {

                    Loading.Show -> {
                        CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    Loading.Hide -> {
                        Button(
                            onClick = {
                                // Podrías añadir validación básica aquí si quieres antes de llamar al VM
                                if (email.isNotBlank() && password.isNotBlank()) {
                                    authViewModel.onLoginClicked(email, password)
                                } else {
                                    // Considera mostrar un Snackbar o mensaje para campos vacíos
                                    // scope.launch { snackbarHostState.showSnackbar("Email y contraseña no pueden estar vacíos.") }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            // enabled se maneja por el estado general de los TextFields y el propio estado de carga
                        ) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    }
}
