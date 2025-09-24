package io.rndev.auth_domain

interface AuthRepository {
    suspend fun login(username: String, password: String): User
}
