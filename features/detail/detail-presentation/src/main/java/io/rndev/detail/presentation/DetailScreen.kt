package io.rndev.detail.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rndev.core.common.ErrorState
import io.rndev.core.common.getBalanceColor
import io.rndev.detail.domain.model.Account
import io.rndev.detail.domain.model.Balance
import io.rndev.detail.domain.model.Transaction
import kotlin.math.abs

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
                nickname = account.nickname,
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
fun AccountHeader(
    nickname: String?,
    accountType: String,
    accountSubType: String,
    currentBalanceDisplay: String, // Already a String from domain model
    currency: String,
    openingDateText: String, // Pre-formatted date string
    accountDescriptionText: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = nickname ?: "Nombre de Cuenta no disponible",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "$accountType - $accountSubType",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Saldo Actual",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row {
                    val balanceValue =
                        currentBalanceDisplay.replace(",", "").toDoubleOrNull() ?: 0.0
                    Text(
                        text = currentBalanceDisplay,
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 32.sp),
                        fontWeight = FontWeight.ExtraBold,
                        color = getBalanceColor(balanceValue)
                    )
                    Text(
                        text = currency,
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                        fontWeight = FontWeight.SemiBold,
                        color = getBalanceColor(balanceValue),
                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                    )
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Apertura",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = openingDateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        accountDescriptionText?.let {
            if (it.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun BalanceCard(
    balanceType: String,
    creditDebitIndicatorText: String,
    amountValue: Double,
    currencyCode: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccountBalanceWallet,
                contentDescription = "Tipo de Saldo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = balanceType,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = creditDebitIndicatorText.replaceFirstChar { it.titlecase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${
                    String.format(
                        "%.2f",
                        amountValue
                    )
                } $currencyCode", // Format Double to String
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = getBalanceColor(amountValue),
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TransactionRow(
    descriptionText: String?,
    bookingDateText: String,
    amountValue: Double,
    currencyCode: String,
    isCredit: Boolean, // Derived from creditDebitIndicator
    // onClick: () -> Unit = {}, // Add if click action is needed for the row
    modifier: Modifier = Modifier
) {

    val amountColor = getBalanceColor(amountValue)
    val transactionIcon =
        if (isCredit) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown
    val sign = when {
        amountValue == 0.0 -> ""
        isCredit -> "+"
        else -> "-"
    }
    val formattedAmount =
        sign + String.format("%.2f", abs(amountValue)) // Show absolute amount with sign

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { /* onClick() */ } // Call onClick if defined and needed
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(amountColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = transactionIcon,
                contentDescription = if (isCredit) "Ingreso" else "Egreso",
                tint = amountColor,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = descriptionText ?: "Sin descripción",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = "Fecha",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = bookingDateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formattedAmount,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
            Text(
                text = currencyCode,
                style = MaterialTheme.typography.labelSmall,
                color = amountColor.copy(alpha = 0.7f)
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = "Ver detalle",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 8.dp)
        )
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
