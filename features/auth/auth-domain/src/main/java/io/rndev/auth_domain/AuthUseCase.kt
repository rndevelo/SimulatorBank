package io.rndev.auth_domain

import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        // Aquí puedes añadir validaciones de las credenciales antes de llamar al repositorio
        if (username.isBlank() || password.isBlank()) {
//            return Result.failure(AuthException.InvalidInput("Email y contraseña no pueden estar vacíos."))
        }
        // Podrías añadir más validaciones (formato de email, longitud de contraseña, etc.)

        return authRepository.login(username, password)
    }
}
