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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
    viewModel: DetailViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    // Extraer strings fuera de lambdas de semantics
    val backButtonDescription = stringResource(id = R.string.detail_back_button_description)
    val loadingDescription = stringResource(id = R.string.detail_loading_description)
    val noDataDescription = stringResource(id = R.string.detail_no_data_description)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        state.account?.nickname
                            ?: stringResource(id = R.string.detail_default_title)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics {
                            contentDescription = backButtonDescription
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null // ya lo gestiona semantics
                        )
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
                state.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.semantics {
                        contentDescription = loadingDescription
                    }
                )

                state.error != null -> ErrorState(
                    message = stringResource(
                        id = R.string.detail_error_message,
                        state.error ?: ""
                    )
                )

                state.account != null -> {
                    AccountDetailContent(
                        account = state.account!!,
                        balances = state.balances,
                        transactions = state.transactions
                    )
                }

                else -> ErrorState(message = noDataDescription)
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
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.semantics {
            contentDescription = title // título leído por screen reader
        }
    )
}

