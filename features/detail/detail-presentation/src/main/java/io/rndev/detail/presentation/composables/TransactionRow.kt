package io.rndev.detail.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.rndev.core.common.getBalanceColor

@SuppressLint("DefaultLocale")
@Composable
fun TransactionRow(
    descriptionText: String?,
    bookingDateText: String,
    amountValue: Double,
    currencyCode: String,
    isCredit: Boolean, // Derived from creditDebitIndicator
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
        TransactionName(descriptionText, bookingDateText)
        Spacer(modifier = Modifier.width(12.dp))
        TransactionCount(sign, amountValue, amountColor, currencyCode)
    }
}

@Composable
private fun RowScope.TransactionName(
    descriptionText: String?,
    bookingDateText: String
) {
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
}

@Composable
private fun TransactionCount(
    sign: String,
    amountValue: Double,
    amountColor: Color,
    currencyCode: String
) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = sign + amountValue,
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
