package io.rndev.detail.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Transaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Cuenta", style = MaterialTheme.typography.titleLarge) }
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
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text("Error: ${state.error}")
                state.account != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Account info
                        item {
                            AccountDetailItem(account = state.account!!)
                        }

                        // Balances
                        item {
                            Text(
                                text = "Balances",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            state.balances.forEach { balance ->
                                BalanceItem(balance)
                            }
                        }

                        // Transactions
                        item {
                            Text(
                                text = "Transacciones",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        items(state.transactions) { transaction ->
                            TransactionItem(transaction)
                        }
                    }
                }
                else -> Text("No se encontraron datos de la cuenta.")
            }
        }
    }
}

@Composable
fun AccountDetailItem(account: Account) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tipo: ${account.type}", style = MaterialTheme.typography.bodySmall)
                Text("Subtipo: ${account.subType}", style = MaterialTheme.typography.bodySmall)
                Text("Desde: ${account.openingDate}", style = MaterialTheme.typography.bodySmall)
                Text(
                    text = "${account.balance} ${account.currency}",
                    fontWeight = FontWeight.Bold,
                    color = if (!account.balance.contains("-")) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun BalanceItem(balance: Balance) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Text("${balance.amount} ${balance.currency}")
        Text(balance.creditDebitIndicator)
        Text(balance.type)
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = transaction.description ?: "Sin descripción",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Monto: ${transaction.amount} ${transaction.currency}")
                Text(transaction.creditDebitIndicator)
            }
            Text("Referencia: ${transaction.reference}", style = MaterialTheme.typography.bodySmall)
            Text(
                "Fecha: ${transaction.bookingDateTime}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
