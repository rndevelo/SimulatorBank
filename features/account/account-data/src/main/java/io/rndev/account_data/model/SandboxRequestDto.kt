package io.rndev.account_data.model

import kotlinx.serialization.Serializable

// Sandbox
@Serializable
data class SandboxRequestDto(
    val accountId: String,
    val operation: String,
    val amount: Double
)

@Serializable
data class SandboxResponseDto(
    val sandboxId: String,
    val message: String?
)
