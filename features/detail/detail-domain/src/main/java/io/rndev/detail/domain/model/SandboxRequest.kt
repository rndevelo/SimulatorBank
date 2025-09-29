package io.rndev.detail.domain.model

// Sandbox operation
data class SandboxRequest(
    val accountId: String,
    val operation: String,
    val amount: Double
)
