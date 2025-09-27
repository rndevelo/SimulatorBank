package io.rndev.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//REQUEST
@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

//RESPONSE
@Serializable
data class AuthDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("user")
    val user: UserDto
)

@Serializable
data class UserDto(
    val id: String,
    val username: String,
    val name: String
)
