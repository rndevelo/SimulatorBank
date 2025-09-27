package io.rndev.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.graphics.vector.ImageVector // No se usa directamente aquí
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
// import androidx.compose.ui.unit.sp // No se usa directamente aquí
import androidx.hilt.navigation.compose.hiltViewModel
import io.rndev.domain.Account // Necesario para extraer los datos

// Helper de AccountScreen (similar al de DetailScreen)
fun getAccountBalanceColor(balance: String?): Color {
    if (balance == null) return Color.Gray
    return try {
        val numericBalance = balance.replace(",", "").toDoubleOrNull() ?: 0.0
        when {
            numericBalance > 0.0 -> Color(0xFF008000) // Dark Green
            numericBalance < 0.0 -> Color(0xFFD32F2F) // Dark Red
            else -> Color.Gray
        }
    } catch (e: NumberFormatException) {
        Color.Gray // En caso de que el string no sea un número válido
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    accountsViewModel: AccountsViewModel = hiltViewModel(),
    onAccountClick: (String) -> Unit = {}
) {
    val state by accountsViewModel.accountUiState.collectAsState() // Asume que AccountUiState tiene isLoading, accounts, error

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Cuentas") },
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
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                state?.isLoading == true -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state?.error != null -> {
                    ErrorState(message = "Error: ${state?.error}", modifier = Modifier.align(Alignment.Center))
                }
                state?.accounts.isNullOrEmpty() -> {
                    EmptyState(message = "No hay cuentas para mostrar.", modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state!!.accounts, key = { it.id }) { account ->
                            // Extraer los parámetros para AccountCardItem
                            val descriptionText = account.description ?: (account.type + if(account.subType.isNotBlank()) " - ${account.subType}" else "")
                            AccountCardItem(
                                nickname = account.nickname,
                                descriptionText = descriptionText,
                                balanceAmount = account.balance, // account.balance es String no nulo en el modelo
                                currency = account.currency,
                                onClick = { onAccountClick(account.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountCardItem(
    // Parámetros primitivos en lugar del objeto Account completo
    nickname: String?,
    descriptionText: String,
    balanceAmount: String, // String no nulo como en el modelo Account
    currency: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBalance,
                    contentDescription = "Icono de cuenta",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nickname ?: "Cuenta sin nombre",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                 Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = balanceAmount, // Ya es String, el helper maneja la lógica de parseo si es necesario
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = getAccountBalanceColor(balanceAmount)
                    )
                    Text(
                        text = currency,
                        style = MaterialTheme.typography.labelSmall,
                        color = getAccountBalanceColor(balanceAmount).copy(alpha = 0.7f)
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
    }
}

@Composable
fun ErrorState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.ErrorOutline,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
