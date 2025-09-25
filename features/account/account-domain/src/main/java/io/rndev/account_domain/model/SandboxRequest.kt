package io.rndev.account_domain.model

// Sandbox operation
data class SandboxRequest(
    val accountId: String,
    val operation: String,
    val amount: Double
)
