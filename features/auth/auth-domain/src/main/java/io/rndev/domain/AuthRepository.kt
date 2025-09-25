package io.rndev.domain

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}
