package io.rndev.detail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.rndev.core.common.ErrorState
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Transaction
import io.rndev.detail.presentation.composables.AccountHeader
import io.rndev.detail.presentation.composables.BalanceCard
import io.rndev.detail.presentation.composables.TransactionRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel, // Assuming DetailViewModel provides the state
    onNavigateBack: () -> Unit // Callback for back navigation
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.account?.nickname ?: "Detalle de Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> ErrorState(message = "Error: ${state.error}")
                state.account != null -> {
                    AccountDetailContent(
                        account = state.account!!, // Pass the full account object here
                        balances = state.balances,   // Pass the full balance list
                        transactions = state.transactions // Pass the full transaction list
                    )
                }

                else -> ErrorState(message = "No se encontraron datos de la cuenta.")
            }
        }
    }
}

@Composable
fun AccountDetailContent(
    account: Account, // Still receiving the full object to extract data
    balances: List<Balance>,
    transactions: List<Transaction>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            AccountHeader(
                accountType = account.type,
                accountSubType = account.subType,
                currentBalanceDisplay = account.balance, // balance is already a String
                currency = account.currency,
                openingDateText = account.openingDate.split("T").firstOrNull()
                    ?: account.openingDate,
                accountDescriptionText = account.description,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }

        if (balances.isNotEmpty()) {
            item {
                SectionTitle(
                    title = "Saldos de la Cuenta",
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 8.dp
                    )
                )
            }
            items(balances, key = { it.type + it.currency }) { balance ->
                BalanceCard(
                    balanceType = balance.type,
                    creditDebitIndicatorText = balance.creditDebitIndicator,
                    amountValue = balance.amount, // Double
                    currencyCode = balance.currency,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
            item {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            }
        }

        if (transactions.isNotEmpty()) {
            item {
                SectionTitle(
                    title = "Movimientos Recientes",
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 8.dp
                    )
                )
            }
            items(transactions, key = { it.transactionId }) { transaction ->
                val isCredit = transaction.creditDebitIndicator.equals("Credit", ignoreCase = true)
                TransactionRow(
                    descriptionText = transaction.description,
                    bookingDateText = transaction.bookingDateTime.split("T").firstOrNull()
                        ?: transaction.bookingDateTime,
                    amountValue = transaction.amount, // Double
                    currencyCode = transaction.currency,
                    isCredit = isCredit,
                    // onClick = { /* Define action if needed */ }
                    modifier = Modifier // Pass modifier if TransactionRow defines it
                )
                if (transactions.last() != transaction) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 72.dp, end = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                }
            }
        } else {
            item {
                Text(
                    "No hay movimientos recientes.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface, // More prominent than onSurfaceVariant
        modifier = modifier
    )
}


//@Preview(showBackground = true)
//@Composable
//fun AccountDetailContentPreview() {
//    AccountDetailContent(
//        account = Account(),
//        balances = listOf(Balance(currency = "USD", amount = 1000.0)),
//        transactions = listOf(
//            Transaction(
//                transactionId = "1",
//                date = "2023-10-01",
//                description = "Purchase",
//                amount = -50.0
//            ),
//            Transaction(transactionId = "2", date = "2023")
//        )
//    )
//}
