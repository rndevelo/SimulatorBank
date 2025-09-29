package io.rndev.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.password
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccessNavigation: () -> Unit = {}, // Callback para navegar tras login exitoso
) {
    val snackBarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }

    // Escucha los eventos del ViewModel para mostrar Snackbars o navegar
    LaunchedEffect(key1 = Unit) {
        authViewModel.events.collect { event ->
            when (event) {
                is AuthUiEvent.Idle -> false
                is AuthUiEvent.Loading -> true
                is AuthUiEvent.Success -> onLoginSuccessNavigation()
                is AuthUiEvent.Error -> {
                    snackBarHostState.showSnackbar(
                        message = event.error, // El ViewModel ya debería proveer un mensaje amigable
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ) { paddingValues ->
        AuthContent(
            isLoading = isLoading,
            onLoginClicked = { email, password ->
                authViewModel.onLoginClicked(email, password)
            },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthContent(
    isLoading: Boolean,
    onLoginClicked: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp), // Más padding horizontal
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = stringResource(id = R.string.auth_screen_icon_description),
                modifier = Modifier
                    .size(64.dp)
                    .semantics {
                        role = Role.Image
                    },
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(id = R.string.auth_screen_title), // Extráelo a strings.xml
                style = MaterialTheme.typography.headlineLarge, // Más prominente
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { heading() }
            )


            val authEmailLabelText = stringResource(id = R.string.auth_email_label)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(authEmailLabelText) }, // Extráelo a strings.xml
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = authEmailLabelText
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                enabled = !isLoading
            )

            val authPasswordLabelText = stringResource(id = R.string.auth_password_label)

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(authPasswordLabelText) }, // Extráelo a strings.xml
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (email.isNotBlank() && password.isNotBlank()) {
                            onLoginClicked(email, password)
                        }
                    }
                ),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible)
                        stringResource(id = R.string.auth_hide_password_action) // Extráelo
                    else stringResource(id = R.string.auth_show_password_action) // Extráelo

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, description)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        password()
                        contentDescription = authPasswordLabelText
                    },
                enabled = !isLoading
            )

            if (isLoading) {
                val authLoadingDescription = stringResource(id = R.string.auth_loading_description)
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .semantics {
                            contentDescription = authLoadingDescription
                        }
                )
            } else {
                val authLoginButton = stringResource(id = R.string.auth_login_button)
                Button(
                    onClick = {
                        keyboardController?.hide() // Ocultar teclado al hacer clic
                        // Considera añadir validación básica aquí o confiar en el ViewModel/Snackbar para errores
                        if (email.isNotBlank() && password.isNotBlank()) {
                            onLoginClicked(email, password)
                        } else {
                            // O dejar que el ViewModel maneje una lógica de validación más compleja si aplica
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .semantics {
                            contentDescription = authLoginButton
                            role = Role.Button
                        }, // Altura estándar para botones
                    shape = RoundedCornerShape(12.dp), // Bordes redondeados consistentes
                ) {
                    Text(authLoginButton) // Extráelo a strings.xml
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun AuthContentPreview() {
    MaterialTheme {
        AuthContent(
            isLoading = false,
            onLoginClicked = { _, _ -> },
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun AuthContentLoadingPreview() {
    MaterialTheme {
        AuthContent(
            isLoading = true,
            onLoginClicked = { _, _ -> },
        )
    }
}
