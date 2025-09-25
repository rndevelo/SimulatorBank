package io.rndev.domain.model

data class Account(
    val id: String,
    val type: String,        // Personal, Business, etc.
    val subType: String,     // Savings, CurrentAccount, Loan, etc.
    val currency: String,
    val description: String?,
    val nickname: String?,
    val openingDate: String,
    val balance: Double,      // Nuevo campo para mostrar saldo
)
