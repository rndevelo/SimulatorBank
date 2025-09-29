package io.rndev.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.rndev.core.common.getBalanceColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    accountsViewModel: AccountsViewModel = hiltViewModel(),
    onAccountClick: (String) -> Unit = {}
) {
    val state by accountsViewModel.accountUiState.collectAsState()

    val screenDescription = stringResource(R.string.account_screen_overall_description)
    val loadingDescription = stringResource(R.string.accounts_loading_description)
    val loadingStateDescription = stringResource(R.string.loading_in_progress)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.account_screen_title),
                        modifier = Modifier.semantics { heading() }
                    )
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
                .padding(paddingValues)
                .semantics { contentDescription = screenDescription },
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics {
                                contentDescription = loadingDescription
                                stateDescription = loadingStateDescription
                            }
                    )
                }

                state.error != null -> {
                    ErrorState(
                        message = stringResource(id = R.string.generic_error_prefix) + state.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.accounts.isEmpty() -> {
                    EmptyState(
                        message = stringResource(id = R.string.accounts_empty_list),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.accounts, key = { it.id }) { account ->
                            val descriptionText = account.description
                                ?: (account.type + if (account.subType.isNotBlank()) " - ${account.subType}" else "")

                            AccountCardItem(
                                nickname = account.nickname,
                                descriptionText = descriptionText,
                                balanceAmount = account.balance,
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
    nickname: String?,
    descriptionText: String,
    balanceAmount: String,
    currency: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentDesc = buildString {
        append(nickname ?: stringResource(id = R.string.account_card_default_nickname))
        append(". ")
        append(descriptionText)
        append(". ")
        append(stringResource(id = R.string.account_balance_accessibility, balanceAmount, currency))
    }

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = contentDesc
                role = Role.Button
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundIconContent()
            Spacer(modifier = Modifier.width(16.dp))
            AccountNameContent(nickname, descriptionText)
            Spacer(modifier = Modifier.width(12.dp))
            AccountDataContent(balanceAmount, currency)
        }
    }
}

@Composable
private fun RoundIconContent() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.AccountBalance,
            contentDescription = stringResource(id = R.string.account_card_icon_description),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun RowScope.AccountNameContent(
    nickname: String?,
    descriptionText: String
) {
    Column(modifier = Modifier.weight(1f)) {
        Text(
            text = nickname ?: stringResource(id = R.string.account_card_default_nickname),
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
}

@Composable
private fun AccountDataContent(balanceAmount: String, currency: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = balanceAmount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = getBalanceColor(balanceAmount.toDoubleOrNull() ?: 0.0)
            )
            Text(
                text = currency,
                style = MaterialTheme.typography.labelSmall,
                color = getBalanceColor(balanceAmount.toDoubleOrNull() ?: 0.0).copy(alpha = 0.7f)
            )
        }
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = stringResource(id = R.string.account_card_view_detail_action),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { contentDescription = message },
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

@Composable
fun ErrorState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .semantics { contentDescription = message },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.ErrorOutline,
            contentDescription = stringResource(id = R.string.generic_error_icon_description),
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
