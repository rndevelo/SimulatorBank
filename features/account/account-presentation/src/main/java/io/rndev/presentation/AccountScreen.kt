package io.rndev.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.rndev.domain.model.Account

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    val state by accountsViewModel.accountUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Cuentas", style = MaterialTheme.typography.titleLarge) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                state?.isLoading == true -> CircularProgressIndicator()

                state?.accounts.isNullOrEmpty() -> Text("No hay cuentas para mostrar.")
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state!!.accounts, key = { it.id }) { account ->
                        AccountElegantItem(
                            account = account,
                            onClick = { println("Clicked account: ${account.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AccountElegantItem(
    account: Account,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.nickname ?: "Cuenta sin nombre",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = account.description ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "${account.balance} ${account.currency}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (account.balance >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tipo: ${account.type}", style = MaterialTheme.typography.bodySmall)
                Text("Subtipo: ${account.subType}", style = MaterialTheme.typography.bodySmall)
                Text("Desde: ${account.openingDate}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

// Suponiendo que tu modelo Account se ve algo así (en :account:account-domain)
// package io.rndev.account_domain.model
// data class Account(
//     val id: String,
//     val name: String,
//     val accountNumber: String,
//     val balance: String, // O un tipo más apropiado como BigDecimal
//     val currency: String
// )

// Suponiendo que tu GetAccountsUseCase se ve algo así (en :account:account-domain)
// package io.rndev.account_domain.usecase
// import io.rndev.account_domain.model.Account
// import io.rndev.account_domain.repository.AccountRepository
// class GetAccountsUseCase(private val repository: AccountRepository) {
//     suspend operator fun invoke(): Result<List<Account>> = repository.getAccounts()
// }

// Y tu AccountException (en :core:common o :account:account-domain)
// package io.rndev.core.common.exception
// open class AppException(message: String?, cause: Throwable? = null) : Throwable(message, cause) {
//     object NetworkError : AppException("Error de red.")
//     data class ServerError(val code: Int, val originalMessage: String?) : AppException("Error del servidor $code")
//     data class UnknownError(val detailedMessage: String?, override val cause: Throwable? = null) : AppException(detailedMessage ?: "Error desconocido", cause)
// }
// sealed class AccountException(message: String?, cause: Throwable? = null) : AppException(message, cause) {
//    object NoAccountsFound : AccountException("No se encontraron cuentas", null) // Ejemplo específico
// }
