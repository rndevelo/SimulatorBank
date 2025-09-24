package io.rndev.auth_domain

import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): User {
        return repository.login(username, password)
    }
}
