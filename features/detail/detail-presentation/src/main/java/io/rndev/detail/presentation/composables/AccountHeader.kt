package io.rndev.detail.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rndev.core.common.getBalanceColor

@Composable
fun AccountHeader(
    accountType: String,
    accountSubType: String,
    currentBalanceDisplay: String, // Already a String from domain model
    currency: String,
    openingDateText: String, // Pre-formatted date string
    accountDescriptionText: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$accountType - $accountSubType",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
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
